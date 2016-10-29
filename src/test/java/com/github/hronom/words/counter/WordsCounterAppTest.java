package com.github.hronom.words.counter;

import com.github.hronom.words.counter.controllers.RootController;
import com.github.hronom.words.counter.services.TestWordsService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WordsCounterAppTest {
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new RootController(new TestWordsService()))
            .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test1() throws Exception {
        mockMvc.perform(post("/ask").param("word", "It"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":1}"));
    }

    @Test
    public void test2() throws Exception {
        mockMvc.perform(post("/ask").param("word", "it"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":1}"));
    }

    @Test
    public void test3() throws Exception {
        mockMvc.perform(post("/ask").param("word", "King's"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"textsCount\":1,\"requestsCount\":1}"));
    }

    @Test
    public void test4() throws Exception {
        mockMvc.perform(post("/ask").param("word", "to"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"textsCount\":2,\"requestsCount\":1}"));
    }

    @Test
    public void test5() throws Exception {
        mockMvc.perform(post("/ask").param("word", "to"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"textsCount\":2,\"requestsCount\":1}"));

        mockMvc.perform(post("/ask").param("word", "to"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"textsCount\":2,\"requestsCount\":2}"));

        mockMvc.perform(post("/ask").param("word", "to"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"textsCount\":2,\"requestsCount\":3}"));
    }
}