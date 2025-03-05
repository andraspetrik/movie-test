package com.test.movie.movietest.movie;

import org.springframework.data.domain.Page;

public record GetMoviesResult(Page<Movie> movies) {
}
