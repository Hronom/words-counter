package com.github.hronom.words.counter.controllers;

import com.github.hronom.words.counter.controllers.pojos.WordStatistic;
import com.github.hronom.words.counter.services.WordsService;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class RootController {
    private final WordsService wordsService;

    @Autowired
    public RootController(WordsService wordsServiceArg) {
        wordsService = wordsServiceArg;
    }

    @ApiOperation(value = "Return link to the SWAGGER UI.")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Something bad happens.")
        })
    @RequestMapping(value = "/", method = {RequestMethod.GET}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity root() {
        return ResponseEntity.ok("<a href=\"/swagger-ui.html#!\">SWAGGER UI</a>");
    }

    @ApiOperation(value = "Return statistic for specified word.")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Something bad happens.")
        })
    @RequestMapping(value = "/ask", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity ask(
        @ApiParam(name = "word", value = "must be an English word (case sensitive)", required = true)
        @RequestParam(value = "word", required = true) String word
    ) {
        try {
            WordStatistic wordStatistic = new WordStatistic(
                wordsService.getWordTextsCount(word),
                wordsService.getWordRequestsCount(word)
            );
            return ResponseEntity.ok(wordStatistic);
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(ExceptionUtils.getStackTrace(exception));
        }
    }
}
