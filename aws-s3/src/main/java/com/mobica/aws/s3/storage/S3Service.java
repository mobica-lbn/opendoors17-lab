package com.mobica.aws.s3.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private static final String BUCKET_NAME = "opendoors17-dev-test";

    private final AmazonS3 amazonS3;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    public Optional<String> getFileContent(String filename) {

        // TODO Create object of S3Object by use amazonS3.getObject and GetObjectRequest
        try (S3Object objectPortion /* = */) {
            return Optional.of(IOUtils.toString(objectPortion.getObjectContent()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public List<String> getAllFiles() {

        // TODO Create object of ObjectListing by use amazonS3.listObjects
        ObjectListing objectListing /* = */;
        return objectListing.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public void saveFile(MultipartFile multipart) throws IOException {
        String fileName = multipart.getOriginalFilename();
        Path write = Files.write(Paths.get(fileName), multipart.getBytes());
        File fileToUpload = write.toFile();

        // TODO Use amazonS3.putObject with new PutObjectRequest

        fileToUpload.delete();
    }

}
