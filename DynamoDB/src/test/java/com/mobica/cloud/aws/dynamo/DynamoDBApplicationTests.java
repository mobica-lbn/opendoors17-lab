package com.mobica.cloud.aws.dynamo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mobica.cloud.aws.dynamo.service.DynamoDBService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamoDBApplicationTests {

  @Autowired
  private DynamoDBService service;

  @Test
  public void getItem() {
    assertNotNull(service.findById(1));
  }

  @Test
  public void getItemWrong() {
    assertNull(service.findById(-1000));
  }

  @Test
  public void getAttribute() {
    assertTrue(service.findById(1).hasAttribute("title"));
  }

  @Test
  public void fetchAll() {
    assertTrue(service.fetchAll().size() > 0);
  }

}
