package com.github.hronom.words.counter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WordsCounterAppTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test1() throws Exception {
        mockMvc.perform(post("/ask").param("word", "It"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":1}"));
    }

    @Test
    public void test2() throws Exception {
        mockMvc.perform(post("/ask").param("word", "it"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":1}"));
    }

    @Test
    public void test3() throws Exception {
        mockMvc.perform(post("/ask").param("word", "King's"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":1}"));
    }

    @Test
    public void test4() throws Exception {
        mockMvc.perform(post("/ask").param("word", "to"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"textsCount\":2,\"requestsCount\":1}"));
    }

    @Test
    public void test5() throws Exception {
        mockMvc.perform(post("/ask").param("word", "Cases"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":1}"));

        mockMvc.perform(post("/ask").param("word", "Cases"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":2}"));

        mockMvc.perform(post("/ask").param("word", "Cases"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":3}"));
    }
}