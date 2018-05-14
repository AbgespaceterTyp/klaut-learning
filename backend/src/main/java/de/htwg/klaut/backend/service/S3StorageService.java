package de.htwg.klaut.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceCreationException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import org.apache.commons.io.FileUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
public class S3StorageService implements IS3StorageService {

    private final IOrganizationService organizationService;
    @Value("${amazon.aws.bucketName}")
    private String bucketName;

    private AmazonS3 amazonS3;

    public S3StorageService(IOrganizationService organizationService, AmazonS3 amazonS3) {
        this.organizationService = organizationService;
        this.amazonS3 = amazonS3;
    }

    @Override
    public void deleteFilesForId(CompositeId modelId) throws SourceNotFoundException, ModelNotFoundException {

    }

    @Override
    public Optional<File> getSourceFile(String sourceUrl) throws SourceNotFoundException {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, sourceUrl));
        // TODO get file
        return Optional.empty();
    }

    @Override
    public Optional<String> addSourceFile(MultipartFile file) throws SourceCreationException {
        try {
            String fileName = UUID.randomUUID().toString() + ".txt";
            return addFile(file.getSize(), fileName, file.getInputStream());
        } catch (IOException e) {
            throw new SourceCreationException(file.getName());
        }
    }

    @Override
    public Optional<String> addSourceFile(Word2Vec word2Vec) throws SourceCreationException {
        // Write model to temp directory
        String modelFileName = UUID.randomUUID().toString();
        final File modelFile = new File(System.getProperty("java.io.tmpdir") + modelFileName + ".model");
        WordVectorSerializer.writeWord2VecModel(word2Vec, modelFile);

        try (FileInputStream fileInputStream = new FileInputStream(modelFile)) {
            return addFile(modelFile.length(), modelFileName, fileInputStream);
        } catch (IOException e) {
            throw new SourceCreationException(modelFileName);
        } finally {
            // Remove from temp dir after upload
            FileUtils.deleteQuietly(modelFile);
        }
    }

    private Optional<String> addFile(long contentLength, String fileName, InputStream inputStream) {
        String s3Key = organizationService.getCurrentOrganization() + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        amazonS3.putObject(
                new PutObjectRequest(bucketName, s3Key, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return Optional.of(String.valueOf(amazonS3.getUrl(bucketName, s3Key)));
    }
}
