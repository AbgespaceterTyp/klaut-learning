package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.SourceCreationException;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

public interface IS3StorageService {
    void deleteSourceFile(String sourceUrl);

    Optional<InputStream> getSourceFile(String sourceUrl);

    Optional<String> addSourceFile(MultipartFile file) throws SourceCreationException;

    Optional<String> addSourceFile(Word2Vec word2Vec) throws SourceCreationException;
}
