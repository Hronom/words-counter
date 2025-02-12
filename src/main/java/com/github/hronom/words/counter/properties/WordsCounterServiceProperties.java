package com.github.hronom.words.counter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("words-counter-service")
public class WordsCounterServiceProperties {
    private String textsFolderPath;

    public String getTextsFolderPath() {
        return textsFolderPath;
    }

    public void setTextsFolderPath(String textsFolderPath) {
        this.textsFolderPath = textsFolderPath;
    }
}
