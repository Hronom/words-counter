package com.github.hronom.words.counter;

import com.github.hronom.words.counter.properties.WordsCounterServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WordsCounterServiceProperties.class})
public class WordsCounterApp {
    public static void main(String[] args) {
        SpringApplication.run(WordsCounterApp.class, args);
    }
}
