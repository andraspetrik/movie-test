package com.test.movie.movietest.movie;

import com.test.movie.movietest.aop.Logged;
import com.test.movie.movietest.aop.LoggedExecutionTime;
import com.test.movie.movietest.network.MovieDatabase;
import com.test.movie.movietest.network.MovieDetails;
import com.test.movie.movietest.network.omdb.OmdbService;
import com.test.movie.movietest.network.themoviedb.TMDbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private static final List<String> VALID_OMDB_API_NAMES = List.of("omdb", "omdbapi", "omdb_api");
    OmdbService omdbService;
    TMDbService TMDbService;

    public MovieService(
            @Autowired OmdbService omdbService,
            @Autowired TMDbService TMDbService
            ) {
        this.omdbService = omdbService;
        this.TMDbService = TMDbService;
    }

    @LoggedExecutionTime
    @Logged
    public List<Movie> getMovies(String title, String apiName) {

        log.debug("title: {}", title);
        log.debug("apiName: {}", apiName);

        final var movieDatabase = getMovieDatabase(apiName);

        var searchResult = movieDatabase.searhForMovies(title);
        return searchResult.stream()
                .map(row -> new Movie(row.title(), row.year(), movieDatabase.getDirectors(row.movieId())))
                .toList();
    }

    private MovieDatabase getMovieDatabase(String apiName) {
        MovieDatabase movieDatabase = null;
        if (VALID_OMDB_API_NAMES.contains(apiName)) {
            movieDatabase = omdbService;
        } else {
            movieDatabase = TMDbService;
            // If null, use it as default
        }
        return movieDatabase;
    }
}
