package com.github.hronom.words.counter.controllers.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordStatistic {
    @JsonProperty
    public final Long textsCount;

    @JsonProperty
    public final Long requestsCount;

    public WordStatistic(Long textsCountArg, Long requestsCountArg) {
        textsCount = textsCountArg;
        requestsCount = requestsCountArg;
    }
}
