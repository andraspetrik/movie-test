package com.test.movie.movietest.network;

import java.util.List;
import java.util.Optional;

public interface MovieDatabase {
    public List<SearchResult> searhForMovies(String title);

    public Optional<MovieDetails> getDetails(String movieId);

    public Optional<MovieCredits> getCredits(String creditId);

}
