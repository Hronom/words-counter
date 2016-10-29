package com.github.hronom.words.counter.controllers;

import com.github.hronom.words.counter.controllers.pojos.WordStatistic;
import com.github.hronom.words.counter.services.WordsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    private final WordsService wordsService;

    @Autowired
    public RootController(WordsService wordsServiceArg) {
        wordsService = wordsServiceArg;
    }

    @RequestMapping(value = "/ask", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<WordStatistic> ask(
        @RequestParam(value = "word", required = true) String word
    ) {
        return ResponseEntity.ok(new WordStatistic(
            wordsService.getWordTextsCount(word),
            wordsService.getWordRequestsCount(word)
        ));
    }
}
