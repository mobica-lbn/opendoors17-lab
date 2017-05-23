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
        try (S3Object objectPortion = amazonS3.getObject(new GetObjectRequest(BUCKET_NAME, filename))) {
            return Optional.of(IOUtils.toString(objectPortion.getObjectContent()));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    public List<String> getAllFiles() {
        ObjectListing objectListing = amazonS3.listObjects(new ListObjectsRequest().withBucketName(BUCKET_NAME));
        return objectListing.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public void saveFile(MultipartFile multipart) throws IOException {
        String filename = multipart.getOriginalFilename();
        Path write = Files.write(Paths.get(filename), multipart.getBytes());
        File file = write.toFile();

        // Upload file
        amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, filename, file));

        file.delete();
    }

}
