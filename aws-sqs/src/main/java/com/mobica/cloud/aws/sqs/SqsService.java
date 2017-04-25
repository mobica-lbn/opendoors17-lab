package com.mobica.cloud.aws.sqs;

import com.mobica.cloud.aws.db.DbMessage;
import com.mobica.cloud.aws.db.DbService;
import org.springframework.stereotype.Service;

@Service
public class SqsService {

    private final DbService dbService;
    private final SqsMessageConverter sqsMessageConverter;

    public SqsService(DbService dbService, SqsMessageConverter sqsMessageConverter) {
        this.dbService = dbService;
        this.sqsMessageConverter = sqsMessageConverter;
    }

    public void processMessage(String sqsMessageJson) {
        DbMessage dbMessage = convertFrom(sqsMessageJson);
        dbService.saveMessage(dbMessage);
    }

    private DbMessage convertFrom(String sqsMessageJson) {
        SqsMessage sqsMessage = sqsMessageConverter.fromJson(sqsMessageJson);
        DbMessage dbMessage = new DbMessage(sqsMessage.getId());
        dbMessage.setAuthor(sqsMessage.getAuthor());
        dbMessage.setTitle(sqsMessage.getTitle());
        dbMessage.setIsbn(sqsMessage.getIsbn());
        dbMessage.setKind1(sqsMessage.getKind1());
        dbMessage.setKind2(sqsMessage.getKind2());
        dbMessage.setKind3(sqsMessage.getKind3());
        dbMessage.setKeyword1(sqsMessage.getKeyword1());
        dbMessage.setKeyword2(sqsMessage.getKeyword2());
        return dbMessage;
    }
}
