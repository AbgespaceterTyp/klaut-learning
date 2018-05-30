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
import de.htwg.klaut.backend.repository.IModelRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Service
@Log4j2
public class Word2VecModelService implements IModelService<Word2VecParams> {

    private final IModelRepository modelRepository;
    private final IS3StorageService s3StorageService;
    private final IOrganizationService organizationService;
    private final ModelTrainer modelTrainer;

    private final ScheduledThreadPoolExecutor executor;

    public Word2VecModelService(IModelRepository modelRepository, IS3StorageService s3StorageService, IOrganizationService organizationService, ModelTrainer modelTrainer) {
        this.modelRepository = modelRepository;
        this.s3StorageService = s3StorageService;
        this.organizationService = organizationService;
        this.modelTrainer = modelTrainer;

        executor = new ScheduledThreadPoolExecutor(3);
    }

    @Override
    public Page<Model> getModels(Pageable pageable) {
        log.debug("loading models");
        return modelRepository.findByOrganization(organizationService.getCurrentOrganization(), pageable);
    }

    @Override
    public Model createModel(ModelDto modelDto) throws ModelCreationException {
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
    public void updateModel(CompositeId modelId, ModelDto modelDto) throws ModelNotFoundException {
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
    public void trainModel(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("starting training of model {}", modelId);

        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (!modelOptional.isPresent()) {
            throw new ModelNotFoundException(modelId);
        }
        final Model modelToTrain = modelOptional.get();
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
    public void addSourceFileToModel(CompositeId modelId, MultipartFile file) throws ModelNotFoundException, SourceCreationException {
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
    public void setModelParams(CompositeId modelId, Word2VecParams modelParams) throws ModelNotFoundException {
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
    public void deleteModel(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("deleting model {}", modelId);

        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (!modelOptional.isPresent()) {
            throw new ModelNotFoundException(modelId);
        }

        final Model modelToDelete = modelOptional.get();
        if (StringUtils.isNotEmpty(modelToDelete.getSourceUrl())) {
            s3StorageService.deleteSourceFile(modelToDelete.getSourceUrl());
        }
        modelRepository.deleteById(modelId);
    }

    @Override
    public Collection<ModelTrainingData> getTrainingsData(CompositeId modelId) throws ModelNotFoundException {
        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (!modelOptional.isPresent()) {
            throw new ModelNotFoundException(modelId);
        }
        return modelOptional.get().getTrainingData();
    }
}
