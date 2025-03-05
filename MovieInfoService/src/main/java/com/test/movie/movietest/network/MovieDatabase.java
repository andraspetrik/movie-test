package com.test.movie.movietest.network;

import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieDatabase {
    Page<SearchResult> searhForMovies(String title, Integer pageNum);

    List<String> getDirectors(String movieId);
}
