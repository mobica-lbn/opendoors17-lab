package com.mobica.cloud.aws.dynamo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import java.util.List;
import java.util.Map;

@Service
public class DynamoDBServiceImpl implements DynamoDBService {

  @Autowired
  private AmazonDynamoDB client;

  @Autowired
  private DynamoDB dynamoDB;

  @Value("${dynamodb.table.name}")
  private String tableName;

  @Override
  public Item findById(int id) throws ResourceNotFoundException {
    // implement using dynamoDB
  }

  @Override
  public List<Map<String, AttributeValue>> fetchAll() throws ResourceNotFoundException {
    // implement using client
  }
}
