package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.ModelCreationException;
import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceCreationException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.db.ModelTrainingData;
import de.htwg.klaut.backend.model.db.Word2VecParams;
import de.htwg.klaut.backend.model.dto.ModelDto;
import de.htwg.klaut.backend.model.dto.ModelTrainingDataDto;
import de.htwg.klaut.backend.repository.IModelRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Service
@Log4j2
public class Word2VecModelService implements IModelService<Word2VecParams> {

    private final IModelRepository modelRepository;
    private final IS3StorageService s3StorageService;
    private final IOrganizationService organizationService;
    private final ModelTrainer modelTrainer;
    private final ModelTester modelTester;

    private final ScheduledThreadPoolExecutor executor;

    public Word2VecModelService(IModelRepository modelRepository,
                                IS3StorageService s3StorageService,
                                IOrganizationService organizationService,
                                ModelTrainer modelTrainer,
                                ModelTester modelTester) {
        this.modelRepository = modelRepository;
        this.s3StorageService = s3StorageService;
        this.organizationService = organizationService;
        this.modelTrainer = modelTrainer;
        this.modelTester = modelTester;

        executor = new ScheduledThreadPoolExecutor(3);
    }

    @Override
    public Page<Model> list(Pageable pageable) {
        log.debug("loading models");
        return modelRepository.findByOrganization(organizationService.getCurrentOrganization(), pageable);
    }

    @Override
    public Model create(ModelDto modelDto) throws ModelCreationException {
        log.debug("creating model with values {}", modelDto);
        try {
            final Model model = new Model();
            model.setName(modelDto.getName());
            model.setDescription(modelDto.getDescription());
            model.setAlgorithm(modelDto.getAlgorithm());
            model.setOrganization(organizationService.getCurrentOrganization());
            return modelRepository.save(model);
        } catch (Exception e) {
            throw new ModelCreationException(modelDto.getName());
        }
    }

    @Override
    public void update(CompositeId modelId, ModelDto modelDto) throws ModelNotFoundException {
        log.debug("updating model {} with values {}", modelId, modelDto);
        try {
            final Model modelToUpdate = modelRepository.findById(modelId).get();
            modelToUpdate.setName(modelDto.getName());
            modelToUpdate.setDescription(modelDto.getDescription());
            modelToUpdate.setAlgorithm(modelDto.getAlgorithm());
            modelRepository.save(modelToUpdate);
        } catch (Exception e) {
            throw new ModelCreationException(modelDto.getName());
        }
    }

    @Override
    public void train(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("starting training of model {}", modelId);

        final Model modelToTrain = get(modelId);
        if (StringUtils.isEmpty(modelToTrain.getSourceUrl())) {
            throw new SourceNotFoundException(modelId);
        }

        // Prepare training data
        final ModelTrainingData trainingData = new ModelTrainingData();
        trainingData.setLastTrainingStart(DateTime.now().toDate());
        final Set<ModelTrainingData> modelTrainingDataSet = modelToTrain.getTrainingData();
        if (modelTrainingDataSet == null) {
            modelToTrain.setTrainingData(new HashSet<>());
        }
        modelToTrain.getTrainingData().add(trainingData);
        modelRepository.save(modelToTrain);

        executor.execute(() -> {
            try {
                modelTrainer.train(modelToTrain, trainingData);
            } catch (Exception e) {
                log.error("Failed to train model " + modelId, e);
                // Remove current training data in case of exceptions
                modelToTrain.getTrainingData().remove(trainingData);
            } finally {
                modelRepository.save(modelToTrain);
            }
        });
    }

    @Override
    public void addSourceFile(CompositeId modelId, MultipartFile file) throws ModelNotFoundException, SourceCreationException {
        log.debug("adding source file {} to model {}", file.getName(), modelId);

        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (!modelOptional.isPresent()) {
            throw new ModelNotFoundException(modelId);
        }

        final Model modelToUpdate = modelOptional.get();
        Optional<String> sourceUrlOpt = s3StorageService.addSourceFile(file);
        if (!sourceUrlOpt.isPresent()) {
            throw new SourceCreationException(modelId);
        }

        modelToUpdate.setSourceUrl(sourceUrlOpt.get());
        modelRepository.save(modelToUpdate);
    }

    @Override
    public InputStream getSourceFile(ModelTrainingDataDto modelTrainingDataDto) throws SourceNotFoundException {
        final Optional<InputStream> modelSourceFile = s3StorageService.getSourceFile(modelTrainingDataDto.getModelUrl());
        if(modelSourceFile.isPresent()){
            return modelSourceFile.get();
        }
        throw new SourceNotFoundException(modelTrainingDataDto.getModelUrl());
    }

    @Override
    public void setParams(CompositeId modelId, Word2VecParams modelParams) throws ModelNotFoundException {
        log.debug("set params {} to model {}", modelParams, modelId);

        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (!modelOptional.isPresent()) {
            throw new ModelNotFoundException(modelId);
        }

        final Model modelToUpdate = modelOptional.get();
        modelToUpdate.setParams(modelParams);
        modelRepository.save(modelToUpdate);
    }

    @Override
    public void delete(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("deleting model {}", modelId);

        final Model modelToDelete = get(modelId);
        if (StringUtils.isNotEmpty(modelToDelete.getSourceUrl())) {
            s3StorageService.deleteSourceFile(modelToDelete.getSourceUrl());
        }
        modelRepository.deleteById(modelId);
    }

    @Override
    public Collection<ModelTrainingData> getTrainingData(CompositeId modelId) throws ModelNotFoundException {
        log.debug("trainingsdata for model {}", modelId);

        return get(modelId).getTrainingData();
    }

    @Override
    public Collection<String> test(CompositeId modelId, String testWord) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("test model {} with word {}", modelId, testWord);

        final Model modelToTest = get(modelId);
        final Set<ModelTrainingData> trainingData = modelToTest.getTrainingData();
        if (trainingData == null || trainingData.isEmpty()) {
            throw new SourceNotFoundException(modelId);
        }
        // TODO choose training data to use
        return modelTester.test(trainingData.iterator().next(), testWord);
    }

    private Model get(CompositeId modelId) {
        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (!modelOptional.isPresent()) {
            throw new ModelNotFoundException(modelId);
        }
        return modelOptional.get();
    }
}
