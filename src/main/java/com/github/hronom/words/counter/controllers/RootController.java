package com.github.hronom.words.counter.controllers;

import com.github.hronom.words.counter.controllers.pojos.WordStatistic;
import com.github.hronom.words.counter.services.WordsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RootController {
    private final WordsService wordsService;

    @Autowired
    public RootController(WordsService wordsServiceArg) {
        wordsService = wordsServiceArg;
    }

    @Operation(summary = "Return link to the SWAGGER UI.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful."),
                    @ApiResponse(responseCode = "400", description = "Something bad happens.")
            })
    @RequestMapping(value = "/", method = {RequestMethod.GET}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("<a href=\"/swagger-ui.html#!\">SWAGGER UI</a>");
    }

    @Operation(summary = "Return statistic for specified word.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful.",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = WordStatistic.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Something bad happens.")
            })
    @RequestMapping(value = "/ask", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> ask(
            @Parameter(name = "word", description = "must be an English word (case sensitive)", required = true)
            @RequestParam(value = "word", required = true) String word
    ) {
        try {
            WordStatistic wordStatistic = new WordStatistic(
                    wordsService.getWordTextsCount(word),
                    wordsService.getWordRequestsCount(word)
            );
            return ResponseEntity.ok(wordStatistic);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ExceptionUtils.getStackTrace(exception));
        }
    }
}
