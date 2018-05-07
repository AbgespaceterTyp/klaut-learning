package de.htwg.klaut.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.htwg.klaut.backend.model.db.CompositeId;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Service
public class S3StorageService implements IS3StorageService {

    @Value("${amazon.aws.bucketName}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public void deleteFilesForId(CompositeId modelId) throws Exception {

    }

    @Override
    public File getSourceFile(String sourceUrl) throws Exception {
        return null;
    }

    @Override
    public String addSourceFile(String fileName) throws Exception {
        File sourceFile = new File(fileName);
        try (FileInputStream fileInputStream = new FileInputStream(sourceFile)) {
            // TODO LG add file name as param or parse from file name
            fileName = createFileName("txt");

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(sourceFile.length());
            amazonS3.putObject(
                    new PutObjectRequest(bucketName, fileName, fileInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            String fileUrl = String.valueOf(amazonS3.getUrl(
                    bucketName, //The S3 Bucket To Upload To
                    fileName));
            return fileUrl;
        }
    }

    @Override
    public String addModel(Word2Vec word2Vec) throws Exception {
        return null;
    }

    private String createFileName(String fileEnding) {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString());
        sb.append(".");
        sb.append(fileEnding);
        return sb.toString();
    }
}
