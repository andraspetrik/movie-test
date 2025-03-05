package com.test.movie.movietest.network.themoviedb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TMDbSearchResult(
        Integer page,
        List<TMDbSearchResultRow> results,

        @JsonProperty("total_pages")
        Integer totalPages,

        @JsonProperty("total_results")
        Integer totalResults

) {
}
