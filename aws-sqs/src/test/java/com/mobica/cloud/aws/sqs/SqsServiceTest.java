package com.mobica.cloud.aws.sqs;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqsServiceTest {
    @MockBean
    private SqsService sqsService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private SqsMessageConverter sqsMessageConverter;

    @Value(value = "${aws.sqs.name}")
    private String sqsName;

    @Test
    public void testMessageReceived() throws InterruptedException {
        //given
        CountDownLatch latch = new CountDownLatch(1);
        Long id = -1L;
        String title = "test_title";
        SqsMessage sqsMessage = new SqsMessage(id);
        sqsMessage.setTitle(title);
        String sqsMessageJson = sqsMessageConverter.toJson(sqsMessage);
        StringBuilder received = new StringBuilder();
        doAnswer(invocationOnMock -> {
            received.setLength(0);
            received.append(String.class.cast(invocationOnMock.getArguments()[0]));
            latch.countDown();
            return null;
        }).when(sqsService).processMessage(sqsMessageJson);

        //when
        jmsTemplate.convertAndSend(sqsName, sqsMessageJson);

        //then
        latch.await(10, TimeUnit.SECONDS);
        SqsMessage sqsMessageReceived = sqsMessageConverter.fromJson(received.toString());
        assertEquals(id, sqsMessageReceived.getId());
        assertEquals(title, sqsMessageReceived.getTitle());
    }
}