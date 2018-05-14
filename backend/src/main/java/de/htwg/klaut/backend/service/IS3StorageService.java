package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceCreationException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

public interface IS3StorageService {
    void deleteSourceFile(String sourceUrl) throws SourceNotFoundException;

    Optional<File> getSourceFile(String sourceUrl) throws SourceNotFoundException;

    Optional<String> addSourceFile(MultipartFile file) throws SourceCreationException;

    Optional<String> addSourceFile(Word2Vec word2Vec) throws SourceCreationException;
}
