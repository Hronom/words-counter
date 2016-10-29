package com.github.hronom.words.counter.tokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PackedTokenAttributeImpl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class LuceneEnglishWordsTokenizer implements EnglishWordsTokenizer {

    /**
     * Create empty set of stopwords to override set inside {@link StandardAnalyzer}.
     */
    public final CharArraySet stopWords = new CharArraySet(0, false);
    public final Analyzer analyzer = new StandardAnalyzer(stopWords);

    @Override
    public List<String> tokenize(String text) {
        return tokenize(analyzer, text);
    }

    private List<String> tokenize(Analyzer analyzer, String string) {
        List<String> result = new ArrayList<>();
        try {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(string));
            stream.reset();
            while (stream.incrementToken()) {
                CharTermAttribute charTermAttribute = stream.getAttribute(CharTermAttribute.class);
                PackedTokenAttributeImpl
                    packedTokenAttribute
                    = (PackedTokenAttributeImpl) charTermAttribute;
                result.add(string.substring(
                    packedTokenAttribute.startOffset(),
                    packedTokenAttribute.endOffset()
                ));
            }
            stream.end();
            stream.close();
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
        return result;
    }
}
