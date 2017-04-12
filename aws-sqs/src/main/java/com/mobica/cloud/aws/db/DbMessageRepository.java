package com.mobica.cloud.aws.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DbMessageRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbMessageRepository.class);

    private final DynamoDBMapper mapper;

    public DbMessageRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public void saveMessage(DbMessage dbMessage) {
        //TODO add code responsible for saving db message in dynamo db

        LOGGER.debug("DbMessage saved: " + dbMessage);
    }

    public void removeMessage(Long id) {
        Optional<DbMessage> readMessage = getMessage(id);
        readMessage.ifPresent(msg -> {
            mapper.delete(msg);
            LOGGER.debug("DbMessage deleted: " + msg);
        });
    }

    public Optional<DbMessage> getMessage(Long id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":id", new AttributeValue().withN(String.valueOf(id)));

        DynamoDBQueryExpression<DbMessage> queryExpression = new DynamoDBQueryExpression<DbMessage>()
                .withKeyConditionExpression(String.format("%s = :id", DbMessage.ID_NAME))
                .withExpressionAttributeValues(eav);

        List<DbMessage> queryResult = mapper.query(DbMessage.class, queryExpression);
        return queryResult.isEmpty() ? Optional.empty() : Optional.of(queryResult.get(0));
    }

}
