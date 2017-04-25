package com.mobica.cloud.aws;

import com.mobica.cloud.aws.db.DbMessage;
import com.mobica.cloud.aws.db.DbService;
import com.mobica.cloud.aws.sqs.SqsMessage;
import com.mobica.cloud.aws.sqs.SqsMessageConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AwsSqsApplicationTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private DbService dbService;

    @Autowired
    private SqsMessageConverter sqsMessageConverter;

    @Value(value = "${aws.sqs.name}")
    private String sqsName;

    @Test
    public void messageSentConsumedAndSaved() throws InterruptedException {
        //given
        Long id = -2L;
        String title = "integration_title";
        SqsMessage sqsMessage = new SqsMessage(id);
        sqsMessage.setTitle(title);
        String sqsMessageJson = sqsMessageConverter.toJson(sqsMessage);

        //when
        jmsTemplate.convertAndSend(sqsName, sqsMessageJson);

        //then
        Thread.sleep(5000);
        Optional<DbMessage> messageBy = dbService.getMessageBy(id);
        assertThat(sqsMessage.getId(), is(equalTo(messageBy.get().getId())));
        assertThat(sqsMessage.getTitle(), is(equalTo(messageBy.get().getTitle())));

        //after
        dbService.removeMessageBy(id);
    }
}
