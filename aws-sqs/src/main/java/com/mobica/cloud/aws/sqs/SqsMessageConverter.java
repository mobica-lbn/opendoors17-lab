package com.mobica.cloud.aws.sqs;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class SqsMessageConverter {
    private final Gson gson;

    public SqsMessageConverter() {
        gson = new Gson();
    }

    public SqsMessage fromJson(String sqsMessageJson) {
        return gson.fromJson(sqsMessageJson, SqsMessage.class);
    }

    public String toJson(SqsMessage sqsMessage) {
        return gson.toJson(sqsMessage);
    }
}
