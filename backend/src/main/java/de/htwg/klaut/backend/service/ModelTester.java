package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.ModelTrainingData;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
@Log4j2
public class ModelTester {

    private final IS3StorageService s3StorageService;

    public ModelTester(IS3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    public Collection<String> test(ModelTrainingData trainingData, String testWord) throws SourceNotFoundException {
        final Optional<InputStream> sourceFile = s3StorageService.getSourceFile(trainingData.getModelUrl());

        // Write model to temp directory
        String modelFileName = UUID.randomUUID().toString();
        final File modelFile = new File(System.getProperty("java.io.tmpdir") + modelFileName + ".model");
        try {
            IOUtils.copy(sourceFile.get(), new FileOutputStream(modelFile));
        } catch (IOException e) {
            log.error("Failed to test model", e);
            throw new SourceNotFoundException(trainingData.getModelUrl());
        }
        final Word2Vec word2VecModelToTest = WordVectorSerializer.readWord2VecModel(modelFile);
        return word2VecModelToTest.wordsNearestSum(testWord, 10);
    }
}
