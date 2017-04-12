package com.mobica.cloud.aws.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SqsMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsMessageListener.class);
    private final SqsService sqsService;

    public SqsMessageListener(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    //TODO implement method to receive sqs message
}
