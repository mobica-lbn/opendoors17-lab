package com.mobica.aws.s3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class S3ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void rootSiteShouldContainFormToUploadFile() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(content().string(containsString("File to upload")));
    }

    @Test
    public void shouldUploadFileWithExtentionsCSV() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "test.csv", "text/plain", "Spring Framework".getBytes());
        mockMvc.perform(fileUpload("/").file(multipartFile))
                .andExpect(flash().attribute("message", is("File uploaded")));
    }

    @Test
    public void shouldDisplayFile() throws Exception {
        mockMvc.perform(get("/MyFile.txt"))
                .andExpect(content().string(containsString("0123456789qwertyuiop")));
    }

}
