package com.test.movie.movietest.network;

import com.test.movie.movietest.movie.Movie;

import java.util.List;

public record MovieDetails(List<String> directors) {
    public static MovieDetails empty() {
        return new MovieDetails(List.of());
    }
}
