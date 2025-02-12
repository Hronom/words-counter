package com.github.hronom.words.counter.services;

import com.github.hronom.words.counter.properties.WordsCounterServiceProperties;
import com.github.hronom.words.counter.tokenizer.EnglishWordsTokenizer;
import com.github.hronom.words.counter.tokenizer.LuceneEnglishWordsTokenizer;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;

@Service
public class WordsService implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final WordsCounterServiceProperties wordsCounterServiceProperties;

    private final EnglishWordsTokenizer englishWordsTokenizer = new LuceneEnglishWordsTokenizer();

    private final ConcurrentHashMap<String, LongAccumulator> wordsTextsStatistic = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LongAccumulator> wordsRequestsStatistic = new ConcurrentHashMap<>();

    @Autowired
    public WordsService(WordsCounterServiceProperties wordsCounterServiceProperties) {
        this.wordsCounterServiceProperties = wordsCounterServiceProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadWordsFromTexts();
        LOGGER.info("wordsTextsStatistic map size: {}", wordsTextsStatistic.size());
        LOGGER.info("wordsRequestsStatistic map size: {}", wordsRequestsStatistic.size());
    }

    public Integer getWordTextsCount(String word) {
        LongAccumulator longAccumulator = wordsTextsStatistic.get(word);
        if (longAccumulator != null) {
            return longAccumulator.intValue();
        } else {
            return 0;
        }
    }

    public Integer getWordRequestsCount(String word) {
        LongAccumulator longAccumulator = wordsRequestsStatistic.get(word);
        if (longAccumulator == null) {
            LongAccumulator newLongAccumulator = new LongAccumulator(
                    Long::sum,
                    0
            );
            // Race condition can occur when two threads try to create value, and one goes faster
            // and increase value in accumulator while another thread can replace it by 0.
            longAccumulator = wordsRequestsStatistic.putIfAbsent(word, newLongAccumulator);
            if (longAccumulator == null) {
                longAccumulator = newLongAccumulator;
            }
        }
        longAccumulator.accumulate(1);
        return longAccumulator.intValue();
    }

    protected void loadWordsFromTexts() throws Exception {
        long startTime = System.currentTimeMillis();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
                Paths.get(wordsCounterServiceProperties.getTextsFolderPath()),
                "*.txt"
        )) {
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS) && !Files.isHidden(path)) {
                    LOGGER.info("Loading words from '{}'...", path);
                    try (InputStream stream = Files.newInputStream(path)) {
                        ContentHandler handler = new BodyContentHandler(-1);
                        AutoDetectParser parser = new AutoDetectParser();
                        Metadata metadata = new Metadata();
                        ParseContext context = new ParseContext();
                        parser.parse(stream, handler, metadata, context);
                        String text = handler.toString();
                        LOGGER.info("Content type of file '{}' - '{}'", path, metadata.get(Metadata.CONTENT_TYPE));

                        List<String> words = tokenizeWord(text);
                        for (String word : words) {
                            addWordToMap(word);
                        }
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("Total loading time {} ms.", endTime - startTime);
    }

    protected List<String> tokenizeWord(String word) throws Exception {
        return englishWordsTokenizer.tokenize(word);
    }

    protected void addWordToMap(String word) {
        LongAccumulator longAccumulator = wordsTextsStatistic.get(word);
        if (longAccumulator == null) {
            longAccumulator = new LongAccumulator(Long::sum, 0);
            wordsTextsStatistic.put(word, longAccumulator);
            wordsRequestsStatistic.put(word, new LongAccumulator(Long::sum, 0));
        }
        longAccumulator.accumulate(1);
    }
}