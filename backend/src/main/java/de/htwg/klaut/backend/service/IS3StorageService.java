package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.db.ModelCompositeId;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.File;

public interface IS3StorageService {
    void deleteFilesForId(ModelCompositeId modelId) throws Exception;

    File getSourceFile(String sourceUrl) throws Exception;

    String addSourceFile(String fileName) throws Exception;

    String addModel(Word2Vec word2Vec) throws Exception;
}
