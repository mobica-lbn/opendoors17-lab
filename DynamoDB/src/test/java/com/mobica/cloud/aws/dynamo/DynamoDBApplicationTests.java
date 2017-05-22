package com.mobica.cloud.aws.dynamo;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.mobica.cloud.aws.dynamo.service.DynamoDBService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamoDBApplicationTests {

    @Autowired
    private DynamoDBService service;

    @Test
    public void getItem() {
        //given
        Long id = -1000L;

        //when
        Item item = service.findById(id);

        //then
        assertNull(item);
    }

}
