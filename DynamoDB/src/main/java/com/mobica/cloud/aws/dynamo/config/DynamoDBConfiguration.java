package com.mobica.cloud.aws.dynamo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Configuration
public class DynamoDBConfiguration {

  @Value("${AWS_ACCESS_KEY_ID}")
  private String key;
  @Value("${AWS_SECRET_ACCESS_KEY}")
  private String secret;
  @Value("${AWS_REGION}")
  private String region;

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {

    AWSStaticCredentialsProvider awsCredentials =
        new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secret));

    return AmazonDynamoDBClientBuilder
        .standard()
        .withCredentials(awsCredentials)
        .withRegion(region)
        .build();
  }

  @Bean
  public DynamoDB dynamoDB(AmazonDynamoDB client) {
    return new DynamoDB(client);
  }

}
