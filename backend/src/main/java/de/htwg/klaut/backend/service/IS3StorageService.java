package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.SourceCreationException;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

public interface IS3StorageService {

    /**
     * Deletes the source file for given source url
     * @param sourceUrl the source url to delete
     */
    void deleteSourceFile(String sourceUrl);

    /**
     * @param sourceUrl the source url to get file for
     * @return an {@Link Optional<InputStream>} which source file in case of success
     */
    Optional<InputStream> getSourceFile(String sourceUrl);

    /**
     * Adds the given {@link MultipartFile} to s3 for given organization.
     * @return returns {@Link Optional<String>} with source url in case of success
     * @throws SourceCreationException when failed to upload source file to s3
     */
    Optional<String> addSourceFile(MultipartFile file, String organization) throws SourceCreationException;

    /**
     * Adds the given {@link Word2Vec} to s3 for given organization.
     * @param word2Vec the model to add
     * @param organization the organization to use
     * @return returns {@Link Optional<String>} with source url in case of success
     * @throws SourceCreationException
     */
    Optional<String> addSourceFile(Word2Vec word2Vec, String organization) throws SourceCreationException;

    Optional<String> addImage(MultipartFile image, String organization);

}
