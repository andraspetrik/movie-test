package com.test.movie.movietest.network.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

public record OmdbSearchResult(
        @JsonProperty("Search") Optional<List<OmdbSearchResultRow>> search
) {
}
