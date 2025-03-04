package com.test.movie.movietest.network.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OmdbSearchResultRow(
        @JsonProperty("Title") String title,
        @JsonProperty("Year") String year,
        @JsonProperty("imdbID") String imdbId
) {
}
