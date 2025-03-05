package com.test.movie.movietest.network.themoviedb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record TMDbSearchResultRow(
        @JsonProperty("title") String title,
        @JsonProperty("id") String id,

        @JsonProperty("release_date") LocalDate releaseDate
) {
}
