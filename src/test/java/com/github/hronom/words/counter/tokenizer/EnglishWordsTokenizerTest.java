package com.github.hronom.words.counter.tokenizer;

import org.junit.Test;

public class EnglishWordsTokenizerTest {
    @Test
    public void tokenize() throws Exception {
        String
            text
            = "currently our funding is mostly from Michael Hart's salary at Carnegie-Mellon University";
        {
            LanguagetoolEnglishWordsTokenizer tokenizer = new LanguagetoolEnglishWordsTokenizer();
            System.out.println("LanguageTool: " + tokenizer.tokenize(text));
        }
        {
            LuceneEnglishWordsTokenizer tokenizer = new LuceneEnglishWordsTokenizer();
            System.out.println("LanguageTool: " + tokenizer.tokenize(text));
        }
    }
}