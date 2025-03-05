package com.test.movie.movietest.movie;

import com.test.movie.movietest.aop.Logged;
import com.test.movie.movietest.aop.LoggedExecutionTime;
import com.test.movie.movietest.network.MovieDatabase;
import com.test.movie.movietest.network.omdb.OmdbService;
import com.test.movie.movietest.network.themoviedb.TMDbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    public static final Integer PAGE_SIZE = 20;

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private static final List<String> VALID_OMDB_API_NAMES = List.of("omdb", "omdbapi", "omdb_api");

    private final OmdbService omdbService;
    private final TMDbService TMDbService;

    public MovieService(
            @Autowired OmdbService omdbService,
            @Autowired TMDbService TMDbService
            ) {
        this.omdbService = omdbService;
        this.TMDbService = TMDbService;
    }

    @LoggedExecutionTime
    @Logged
    public Page<Movie> getMovies(String title, String apiName, String pageInp) {

        log.debug("title: {}", title);
        log.debug("apiName: {}", apiName);


        var pageNum = 1;
        try {
            pageNum = Integer.parseInt(pageInp);
        } catch (NumberFormatException e) { /* do nothing */ }

        final var movieDatabase = getMovieDatabase(apiName);

        var searchResult = movieDatabase.searhForMovies(title, pageNum);

        return searchResult
                .map(row -> new Movie(row.title(), row.year(), movieDatabase.getDirectors(row.movieId())));
    }

    private MovieDatabase getMovieDatabase(String apiName) {
        MovieDatabase movieDatabase;
        if (VALID_OMDB_API_NAMES.contains(apiName)) {
            movieDatabase = omdbService;
        } else {
            movieDatabase = TMDbService;
            // If null, use it as default
        }
        return movieDatabase;
    }
}
