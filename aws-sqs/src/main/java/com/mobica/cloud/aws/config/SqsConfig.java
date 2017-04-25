package com.mobica.cloud.aws.config;


import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
@EnableJms
public class SqsConfig {
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(SQSConnectionFactory connectionFactory) {
        final String concurrency = "3-10";
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency(concurrency);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public JmsTemplate defaultJmsTemplate(SQSConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    @Bean
    public SQSConnectionFactory createSqsConnection(AWSCredentialsProvider awsCredentialsProvider, Regions region) {
        return SQSConnectionFactory.builder()
                .withRegion(Region.getRegion(region))
                .withAWSCredentialsProvider(awsCredentialsProvider)
                .build();
    }
}
