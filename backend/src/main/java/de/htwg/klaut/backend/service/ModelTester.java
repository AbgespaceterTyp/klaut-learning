package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.SourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Component
@Log4j2
public class ModelTester {

    private final IS3StorageService s3StorageService;

    public ModelTester(IS3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    public Collection<String> test(String sourceUrl, String testWord) throws SourceNotFoundException {
        final Optional<InputStream> sourceFile = s3StorageService.getSourceFile(sourceUrl);

        // Write model to temp directory
        String modelFileName = UUID.randomUUID().toString();
        final File modelFile = new File(System.getProperty("java.io.tmpdir") + modelFileName + ".model");
        modelFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(modelFile)) {
            IOUtils.copy(sourceFile.get(), fos);
            final Word2Vec word2VecModelToTest = WordVectorSerializer.readWord2VecModel(modelFile);
            // Important: test word must be in lower case or dl4j will throw NullPointerException
            // (because all words in vocabulary are stored in lower case)
            try {
                return word2VecModelToTest.wordsNearestSum(testWord.toLowerCase(), 10);
            } catch (NullPointerException e) {
                // Dl4J throws NullPointerException when the given test word is not contained in vocabulary
                // so NullPointerException will be ignored here and a empty collection returned
                log.debug("Failed to find value for test word {}", testWord, e);
                return Collections.emptyList();
            }
        } catch (IOException e) {
            log.error("Failed to test model", e);
            throw new SourceNotFoundException(sourceUrl);
        } finally {
            FileUtils.deleteQuietly(modelFile);
        }
    }
}
