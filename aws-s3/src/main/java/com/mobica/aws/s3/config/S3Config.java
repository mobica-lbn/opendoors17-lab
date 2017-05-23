package com.mobica.aws.s3.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public AmazonS3 amazonS3(AWSCredentialsProvider provider, Regions region) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(provider)
                .withRegion(region)
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
