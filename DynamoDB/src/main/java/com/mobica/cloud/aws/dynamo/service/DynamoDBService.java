package com.mobica.cloud.aws.dynamo.service;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public interface DynamoDBService {

  Item findById(int id);
  List<Map<String, AttributeValue>> fetchAll();
}
