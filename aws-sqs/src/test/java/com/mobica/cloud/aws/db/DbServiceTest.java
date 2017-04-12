package com.mobica.cloud.aws.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbServiceTest {

    @Autowired
    private DbService dbService;

    @Test
    public void testSaveMessage() {
        //given
        Long id = -1L;
        String title = "test_title";
        DbMessage dbMessage = new DbMessage(id);
        dbMessage.setTitle(title);

        //when
        dbService.saveMessage(dbMessage);

        //then
        Optional<DbMessage> messageBy = dbService.getMessageBy(id);
        assertEquals(title, messageBy.get().getTitle());
        assertEquals(id, messageBy.get().getId());

        //after
        dbService.removeMessageBy(id);
    }
}