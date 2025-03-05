package com.test.movie.movietest.network.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OmdbDetailsResult(@JsonProperty("Director") String director) {
}
