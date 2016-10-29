package com.github.hronom.words.counter.tokenizer;

import java.util.List;

public interface EnglishWordsTokenizer {
    List<String> tokenize(String text) throws Exception;
}
