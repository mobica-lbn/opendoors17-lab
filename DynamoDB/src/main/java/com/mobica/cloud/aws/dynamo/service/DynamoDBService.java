package com.mobica.cloud.aws.dynamo.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DynamoDBService {

    @Value("${dynamodb.table.name}")
    private String tableName;

    private final DynamoDB dynamoDB;

    public DynamoDBService(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public Item findById(Long id) {
        //TODO get item
        return null;
    }

    public String fetchAll() {
        // TODO get all items
        return "";
    }
}
