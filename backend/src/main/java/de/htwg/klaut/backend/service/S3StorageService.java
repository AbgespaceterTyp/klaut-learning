package de.htwg.klaut.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import de.htwg.klaut.backend.exception.ModelNotFoundException;
import de.htwg.klaut.backend.exception.SourceNotFoundException;
import de.htwg.klaut.backend.model.db.CompositeId;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.util.ModelSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class S3StorageService implements IS3StorageService {

    @Value("${amazon.aws.bucketName}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

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
    public Optional<String> addSourceFile(String fileName, String organization) throws SourceNotFoundException {
        File sourceFile = new File(fileName);
        try (FileInputStream fileInputStream = new FileInputStream(sourceFile)) {
            // TODO LG add file name as param or parse from file name
            fileName = createFileName("txt");

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(sourceFile.length());
            amazonS3.putObject(
                    new PutObjectRequest(bucketName, fileName, fileInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            return Optional.of(String.valueOf(amazonS3.getUrl(bucketName, fileName)));
        } catch (IOException e) {
            throw new SourceNotFoundException(fileName);
        }
    }

    @Override
    public Optional<String> addModel(Word2Vec word2Vec, String organization) {
        // Write model to temp directory
        String modelFileName = UUID.randomUUID().toString();
        final File modelFile = new File(System.getProperty("java.io.tmpdir") + modelFileName);
        WordVectorSerializer.writeWord2VecModel(word2Vec, modelFile);

        try (FileInputStream fileInputStream = new FileInputStream(modelFile)) {
            String s3Key = organization + "/" + modelFileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(modelFile.length());
            amazonS3.putObject(
                    new PutObjectRequest(bucketName, organization + "/" + s3Key, fileInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            return Optional.of(String.valueOf(amazonS3.getUrl(bucketName, s3Key)));
        } catch (IOException e) {

        } finally {
            // Remove from temp dir after upload
            FileUtils.deleteQuietly(modelFile);
        }
        return Optional.empty();
    }

    private String createFileName(String fileEnding) {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString());
        sb.append(".");
        sb.append(fileEnding);
        return sb.toString();
    }
}
