package com.github.hronom.words.counter.services;

import java.util.List;

public class TestWordsService extends WordsService {

    public TestWordsService() throws Exception {
        super();
    }

    @Override
    protected void loadWordsFromTexts() throws Exception {
        uploadText("It was one of those Cases in which there ought to");
        uploadText("However unfortunate it may have been for the sufferer, the King's malady has been no disservice to the Nation;");
        uploadText("However this can happens");
    }

    private void uploadText(String text) throws Exception {
        List<String> words = tokenizeWord(text);
        for (String word : words) {
            addWordToMap(word);
        }
    }
}