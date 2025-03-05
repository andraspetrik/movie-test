package com.test.movie.movietest.network;

import java.util.List;

public interface MovieDatabase {
    List<SearchResult> searhForMovies(String title);

    List<String> getDirectors(String movieId);
}
