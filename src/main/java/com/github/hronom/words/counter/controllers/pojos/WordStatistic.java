package com.github.hronom.words.counter.controllers.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordStatistic {
    @JsonProperty
    public final Integer textsCount;

    @JsonProperty
    public final Integer requestsCount;

    public WordStatistic(Integer textsCountArg, Integer requestsCountArg) {
        textsCount = textsCountArg;
        requestsCount = requestsCountArg;
    }
}
