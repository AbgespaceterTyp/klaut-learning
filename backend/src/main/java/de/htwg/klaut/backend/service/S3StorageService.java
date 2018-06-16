package de.htwg.klaut.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import de.htwg.klaut.backend.exception.SourceCreationException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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
@Log4j2
public class S3StorageService implements IS3StorageService {

    private static final String ORGANIZATION_IMAGE = "organization_image";

    @Value("${amazon.aws.bucketName}")
    private String bucketName;

    private AmazonS3 amazonS3;

    public S3StorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public void deleteSourceFile(String sourceUrl) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, sourceUrl));
    }

    @Override
    public Optional<InputStream> getSourceFile(String sourceUrl) {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, sourceUrl));
        if (s3Object == null) {
            return Optional.empty();
        }
        return Optional.of(s3Object.getObjectContent());
    }

    @Override
    public Optional<String> addSourceFile(MultipartFile file, String organization) throws SourceCreationException {
        String fileName = UUID.randomUUID().toString() + ".txt";
        try {
            return Optional.of(addFile(file.getSize(), fileName, file.getInputStream(), organization));
        } catch (IOException e) {
            log.error("Failed to add source file " + fileName);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> addSourceFile(Word2Vec word2Vec, String organization) throws SourceCreationException {
        // Write model to temp directory
        String modelFileName = UUID.randomUUID().toString();
        final File modelFile = new File(System.getProperty("java.io.tmpdir") + modelFileName + ".model");
        WordVectorSerializer.writeWord2VecModel(word2Vec, modelFile);

        try (FileInputStream fis = new FileInputStream(modelFile)) {
            return Optional.of(addFile(modelFile.length(), modelFileName, fis, organization));
        } catch (IOException e) {
            log.error("Failed to add source file " + modelFileName);
        } finally {
            FileUtils.deleteQuietly(modelFile);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> addImage(MultipartFile image, String organization) {
        try {
            String filename = image.getOriginalFilename();
            String fileEnding = ".jpg";
            String extension = FilenameUtils.getExtension(filename);
            if (StringUtils.isNotEmpty(extension)) {
                fileEnding = ".".concat(extension);
            }
            return Optional.of(addFile(image.getSize(), ORGANIZATION_IMAGE.concat(fileEnding), image.getInputStream(), organization));
        } catch (IOException e) {
            log.error("Failed to set image for Organization:", organization);
        }
        return Optional.empty();
    }

    private String addFile(long contentLength, String fileName, InputStream inputStream, String organization) {
        String s3Key = organization + "/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        amazonS3.putObject(
                new PutObjectRequest(bucketName, s3Key, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Key;
    }
}
