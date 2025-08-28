package ru.orlov.adrift.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Response;

import java.net.URI;

@Log4j2
@Service
public class StorageService {

    private final String bucketName;
    private final S3Client s3Client;

    public StorageService(
            @Value("${aws.s3.endpoint}") String endpoint,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey,
            @Value("${aws.s3.bucket-name}") String bucketName
    ) {
        AwsCredentials credentials = AwsBasicCredentials
                .create(accessKey, secretKey);

        s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(() -> credentials)
                .build();

        this.bucketName = bucketName;
    }

    public void uploadFileToS3(String filename, byte[] fileContent) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        S3Response response = s3Client.putObject(
                request,
                RequestBody.fromBytes(fileContent)
        );

        log.info("File uploaded to S3: {}", response.sdkHttpResponse().statusCode());
    }
}
