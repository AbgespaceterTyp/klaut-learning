package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.SourceCreationException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import de.htwg.klaut.backend.model.db.ModelTrainingData;
import de.htwg.klaut.backend.model.db.Word2VecParams;
import org.apache.commons.io.IOUtils;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
public class ModelTrainer {

    private final IS3StorageService s3StorageService;

    public ModelTrainer(IS3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    public Model train(Model modelToTrain, ModelTrainingData trainingData, String organization) throws IOException {
        BasicLineIterator iterator = prepareSourceFile(modelToTrain);
        Word2Vec word2VecModel = trainModel(iterator, modelToTrain);

        Optional<String> modelUrlOpt = s3StorageService.addSourceFile(word2VecModel, organization);
        if (!modelUrlOpt.isPresent()) {
            throw new SourceCreationException(new CompositeId(organization, modelToTrain.getId()));
        }

        // Update training data after
        trainingData.setLastTrainingEnd(DateTime.now().toDate());
        trainingData.setModelUrl(modelUrlOpt.get());
        return modelToTrain;
    }

    private BasicLineIterator prepareSourceFile(Model modelToTrain) throws IOException {
        Optional<InputStream> sourceFileOpt = s3StorageService.getSourceFile(modelToTrain.getSourceUrl());
        if (!sourceFileOpt.isPresent()) {
            throw new SourceNotFoundException(modelToTrain.getId());
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(sourceFileOpt.get(), outputStream);
        final ByteArrayInputStream newInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        return new BasicLineIterator(newInputStream);
    }

    private Word2Vec trainModel(BasicLineIterator iterator, Model modelToTrain) {
        // Split on white spaces in the line to list words
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

        // Start training
        word2VecModel.fit();
        return word2VecModel;
    }
}
