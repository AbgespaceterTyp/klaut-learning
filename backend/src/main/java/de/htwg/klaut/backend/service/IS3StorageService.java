package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface IS3StorageService {
    void deleteFilesForId(CompositeId modelId) throws SourceNotFoundException, ModelNotFoundException;

    Optional<File> getSourceFile(String sourceUrl) throws SourceNotFoundException;

    Optional<String> addSourceFile(String fileName) throws SourceNotFoundException;

    Optional<String> addModel(Word2Vec word2Vec);
}
