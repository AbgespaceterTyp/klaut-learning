package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.ModelParams;
import de.htwg.klaut.backend.model.Word2VecParams;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.repository.ModelRepository;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Set;

public class Word2VecModelService implements IModelService {

    private final ModelRepository modelRepository;

    public Word2VecModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Override
    public Page<Model> getModels(Pageable pageable) {
        return modelRepository.findAll(pageable);
    }

    @Override
    public Model createModel(String modelName, String modelDescription) {
        final Model model = new Model();
        model.setName(modelName);
        model.setDescription(modelDescription);
        return modelRepository.save(model);
    }

    @Override
    public void trainModel(String modelId, Set<String> sourceUrls) throws Exception {
        trainModel(modelId, null, Collections.emptySet());
    }

    @Override
    public void trainModel(String modelId, ModelParams modelParams) throws Exception {
        trainModel(modelId, modelParams, Collections.emptySet());
    }

    @Override
    public void trainModel(String modelId, ModelParams modelParams, Set<String> sourceUrls) throws Exception {
        // TODO LG implement
        // store source files
        // load model from db and link new source urls
        // update model for new params if set
        // start training
        // store model again

        // TODO check model type first
        // TODO load from source urls
        try (FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/Der_Dunkelgraf_Norm.txt"))) {
            BasicLineIterator iter = new BasicLineIterator(fileInputStream);
            // Split on white spaces in the line to get words
            DefaultTokenizerFactory t = new DefaultTokenizerFactory();

            t.setTokenPreProcessor(new CommonPreprocessor());

            // TODO check for type before casting it;)
            Word2VecParams word2VecParams = (Word2VecParams) modelParams;
            Word2Vec Word2Vec = new Word2Vec.Builder()
                    .minWordFrequency(word2VecParams.getMinWordFrequency())
                    .iterations(word2VecParams.getIterations())
                    .layerSize(word2VecParams.getLayerSize())
                    .seed(word2VecParams.getSeed())
                    .windowSize(word2VecParams.getWindowSize())
                    .iterate(iter)
                    .tokenizerFactory(t)
                    .build();

            Word2Vec.fit();
        } catch (Exception e) {
            // TODO LG log error and throw exception
            throw e;
        }
    }

    @Override
    public void deleteModel(String modelId) {
        // TODO LG implement
        // load model from db
        // delete files from s3
        modelRepository.deleteById(modelId);
    }
}
