package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.Word2VecParams;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.repository.IModelRepository;
import lombok.extern.log4j.Log4j2;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class Word2VecModelService implements IModelService<Word2VecParams> {

    private final IModelRepository modelRepository;
    private final IS3StorageService s3StorageService;

    public Word2VecModelService(IModelRepository modelRepository, IS3StorageService s3StorageService) {
        this.modelRepository = modelRepository;
        this.s3StorageService = s3StorageService;
    }

    @Override
    public Page<Model> getModels(String organization, Pageable pageable) {
        log.debug("loading models");
        return modelRepository.findByOrOrganization(organization, pageable);
    }

    @Override
    public Model createModel(String modelName, String modelDescription, String organization) {
        log.debug("creating model (name='{}', desc='{}', org='{}')", modelName, modelDescription, organization);

        final Model model = new Model();
        model.setName(modelName);
        model.setDescription(modelDescription);
        model.setOrganization(organization);

        return modelRepository.save(model);
    }

    @Override
    public void trainModel(CompositeId modelId, String organization) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("starting training of model {}", modelId);

        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (!modelOptional.isPresent()) {
            throw new ModelNotFoundException(modelId);
        }
        final Model modelToTrain = modelOptional.get();
        Set<String> sourceUrls = modelToTrain.getSourceUrls();
        if (sourceUrls.isEmpty()) {
            throw new SourceNotFoundException(modelId);
        }

        // TODO LG we just take first source url here, check if it is possible to use more than one source file
        final String sourceUrl = sourceUrls.iterator().next();
        log.debug("loading source url {}", sourceUrl);

        Optional<File> sourceFileOpt = s3StorageService.getSourceFile(sourceUrl);
        if(!sourceFileOpt.isPresent()){
            throw new SourceNotFoundException(modelId);
        }

        try (FileInputStream fileInputStream = new FileInputStream(sourceFileOpt.get())) {
            BasicLineIterator iter = new BasicLineIterator(fileInputStream);
            // Split on white spaces in the line to get words
            DefaultTokenizerFactory t = new DefaultTokenizerFactory();
            t.setTokenPreProcessor(new CommonPreprocessor());

            final Word2VecParams params = (Word2VecParams) modelToTrain.getParams();
            Word2Vec Word2Vec = new Word2Vec.Builder()
                    .minWordFrequency(params.getMinWordFrequency())
                    .iterations(params.getIterations())
                    .layerSize(params.getLayerSize())
                    .seed(params.getSeed())
                    .windowSize(params.getWindowSize())
                    .iterate(iter)
                    .tokenizerFactory(t)
                    .build();

            Word2Vec.fit();
            log.debug("finished training of model {}", modelId);

            // TODO LG how to go on training with an existing model?
            Optional<String> modelUrlOpt = s3StorageService.addModel(Word2Vec, organization);
            if(modelUrlOpt.isPresent()) {
                modelToTrain.setModelUrl(modelUrlOpt.get());
                modelRepository.save(modelToTrain);
            } else {
                // TODO exception
            }
        } catch (IOException e){
            throw new SourceNotFoundException(sourceUrl);
        }
    }

    @Override
    public void addSource(CompositeId modelId, String fileName, String organization) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("adding source file {} to model {}", fileName, modelId);

        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (modelOptional.isPresent()) {
            final Model modelToUpdate = modelOptional.get();
            Optional<String> sourceUrlOpt = s3StorageService.addSourceFile(fileName, organization);
            if(sourceUrlOpt.isPresent()) {
                modelToUpdate.getSourceUrls().add(sourceUrlOpt.get());
                modelRepository.save(modelToUpdate);
            } else {
                // TODO exception
            }
        } else {
            throw new ModelNotFoundException(modelId);
        }
    }

    @Override
    public void setParams(CompositeId modelId, Word2VecParams modelParams) throws ModelNotFoundException {
        log.debug("set params {} to model {}", modelParams, modelId);

        Optional<Model> modelOptional = modelRepository.findById(modelId);
        if (modelOptional.isPresent()) {
            final Model modelToUpdate = modelOptional.get();
            modelToUpdate.setParams(modelParams);
            modelRepository.save(modelToUpdate);
        } else {
            throw new ModelNotFoundException(modelId);
        }
    }

    @Override
    public void deleteModel(CompositeId modelId) throws ModelNotFoundException, SourceNotFoundException {
        log.debug("deleting model {}", modelId);

        s3StorageService.deleteFilesForId(modelId);
        modelRepository.deleteById(modelId);
    }
}
