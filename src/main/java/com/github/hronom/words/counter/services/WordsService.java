package com.github.hronom.words.counter.services;

import com.github.hronom.words.counter.tokenizer.EnglishWordsTokenizer;
import com.github.hronom.words.counter.tokenizer.LuceneEnglishWordsTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;

@Service
public class WordsService {
    private static final Logger logger = LogManager.getLogger();

    private final Path textsPath = Paths.get("texts");

    private final EnglishWordsTokenizer englishWordsTokenizer = new LuceneEnglishWordsTokenizer();

    private ConcurrentHashMap<String, LongAccumulator>
        wordsTextsStatistic
        = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LongAccumulator>
        wordsRequestsStatistic
        = new ConcurrentHashMap<>();

    public WordsService() throws Exception {
        loadWordsFromTexts();
        logger.info("wordsTextsStatistic map size: " + wordsTextsStatistic.size());
        logger.info("wordsRequestsStatistic map size: " + wordsRequestsStatistic.size());
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
                (left, right) -> left + right,
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
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(textsPath, "*.txt")) {
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS) && !Files.isHidden(path)) {
                    logger.info("Loading words from \"" + path + "\"...");
                    try (InputStream stream = Files.newInputStream(path)) {
                        ContentHandler handler = new BodyContentHandler(-1);
                        AutoDetectParser parser = new AutoDetectParser();
                        Metadata metadata = new Metadata();
                        ParseContext context = new ParseContext();
                        parser.parse(stream, handler, metadata, context);
                        String text = handler.toString();
                        logger.info(
                            "Content type of file \"" +
                            path +
                            "\" - \"" +
                            metadata.get(Metadata.CONTENT_TYPE) +
                            "\""
                        );

                        List<String> words = tokenizeWord(text);
                        for (String word : words) {
                            addWordToMap(word);
                        }
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("Total loading time " + (endTime - startTime) + " ms.");
    }

    protected List<String> tokenizeWord(String word) throws Exception {
        return englishWordsTokenizer.tokenize(word);
    }

    protected void addWordToMap(String word) {
        LongAccumulator longAccumulator = wordsTextsStatistic.get(word);
        if (longAccumulator == null) {
            longAccumulator = new LongAccumulator((left, right) -> left + right, 0);
            wordsTextsStatistic.put(word, longAccumulator);
            wordsRequestsStatistic.put(word, new LongAccumulator((left, right) -> left + right, 0));
        }
        longAccumulator.accumulate(1);
    }
}