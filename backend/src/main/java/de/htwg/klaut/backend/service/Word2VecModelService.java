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
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class Word2VecModelService implements IModelService<Word2VecParams> {

    private final IModelRepository modelRepository;
    private final IS3StorageService s3StorageService;
    private final IOrganizationService organizationService;

    public Word2VecModelService(IModelRepository modelRepository, IS3StorageService s3StorageService, IOrganizationService organizationService) {
        this.modelRepository = modelRepository;
        this.s3StorageService = s3StorageService;
        this.organizationService = organizationService;
    }

    @Override
    public Page<Model> getModels(Pageable pageable) {
        log.debug("loading models");
        return modelRepository.findByOrOrganization(organizationService.getCurrentOrganization(), pageable);
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
        Set<String> sourceUrls = modelToTrain.getSourceUrls();
        if (sourceUrls == null || sourceUrls.isEmpty()) {
            throw new SourceNotFoundException(modelId);
        }

        // TODO LG we just take first source url here, check if it is possible to use more than one source file
        final String sourceUrl = sourceUrls.iterator().next();
        Optional<InputStream> sourceFileOpt = s3StorageService.getSourceFile(sourceUrl);
        if (!sourceFileOpt.isPresent()) {
            throw new SourceNotFoundException(modelId);
        }

        updateAndTrainModel(sourceFileOpt.get(), modelToTrain);
    }

    private void updateAndTrainModel(InputStream sourceFile, Model modelToTrain) {
        BasicLineIterator iterator = new BasicLineIterator(sourceFile);
        // Split on white spaces in the line to get words
        DefaultTokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        Word2VecParams params = (Word2VecParams) modelToTrain.getParams();
        Word2Vec word2VecModel = new Word2Vec.Builder()
                .minWordFrequency(params.getMinWordFrequency())
                .iterations(params.getIterations())
                .layerSize(params.getLayerSize())
                .seed(params.getSeed())
                .windowSize(params.getWindowSize())
                .iterate(iterator)
                .tokenizerFactory(t)
                .build();

        // Prepare training data
        final ModelTrainingData trainingData = new ModelTrainingData();
        trainingData.setLastTrainingStart(DateTime.now().toDate());
        final Set<ModelTrainingData> modelTrainingDataSet = modelToTrain.getTrainingData();
        if (modelTrainingDataSet == null) {
            modelToTrain.setTrainingData(new HashSet<>());
        }
        modelToTrain.getTrainingData().add(trainingData);
        modelRepository.save(modelToTrain);

        // Start training
        word2VecModel.fit();

        // TODO LG how to go on training with an existing model?
        Optional<String> modelUrlOpt = s3StorageService.addSourceFile(word2VecModel);
        if (!modelUrlOpt.isPresent()) {
            throw new SourceCreationException(new CompositeId(modelToTrain.getOrganization(), modelToTrain.getId()));
        }

        // Update training data after
        trainingData.setLastTrainingEnd(DateTime.now().toDate());
        trainingData.setModelUrl(modelUrlOpt.get());
        modelRepository.save(modelToTrain);
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

        if (modelToUpdate.getSourceUrls() == null) {
            modelToUpdate.setSourceUrls(new HashSet<>());
        }
        modelToUpdate.getSourceUrls().add(sourceUrlOpt.get());
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
        if (modelToDelete.getSourceUrls() != null) {
            for (String sourceUrlToDelete : modelToDelete.getSourceUrls()) {
                s3StorageService.deleteSourceFile(sourceUrlToDelete);
            }
        }
        modelRepository.deleteById(modelId);
    }
}
