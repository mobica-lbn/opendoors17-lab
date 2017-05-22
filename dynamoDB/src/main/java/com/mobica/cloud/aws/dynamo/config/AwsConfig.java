package com.mobica.cloud.aws.dynamo.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.util.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Bean
    public AWSCredentialsProvider credentialsProvider() {
        return new EnvironmentVariableCredentialsProvider();
    }

    @Bean
    public Regions region() {
        return Regions.fromName(StringUtils.trim(System.getenv("AWS_REGION")));
    }

}
