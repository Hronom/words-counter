package com.github.hronom.words.counter.tokenizer;

import org.languagetool.tokenizers.en.EnglishWordTokenizer;

import java.util.List;

public class LanguagetoolEnglishWordsTokenizer implements EnglishWordsTokenizer {
    private final EnglishWordTokenizer wordTokenizer = new EnglishWordTokenizer();

    @Override
    public List<String> tokenize(String text) throws Exception {
        return wordTokenizer.tokenize(text);
    }
}
