package com.github.hronom.words.counter.tokenizer;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class EnglishWordsTokenizerTest {
    @Test
    public void tokenize() throws Exception {
        String
            text
            = "currently our funding is mostly from Michael Hart's salary at Carnegie-Mellon University";
        LuceneEnglishWordsTokenizer tokenizer = new LuceneEnglishWordsTokenizer();
        List<String> words = tokenizer.tokenize(text);
        assertEquals("currently", words.get(0));
        assertEquals("our", words.get(1));
        assertEquals("funding", words.get(2));
        assertEquals("is", words.get(3));
        assertEquals("mostly", words.get(4));
        assertEquals("from", words.get(5));
        assertEquals("Michael", words.get(6));
        assertEquals("Hart's", words.get(7));
        assertEquals("salary", words.get(8));
        assertEquals("at", words.get(9));
        assertEquals("Carnegie", words.get(10));
        assertEquals("Mellon", words.get(11));
        assertEquals("University", words.get(12));
    }
}