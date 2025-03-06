package com.test.movie.movietest.movie;

import com.test.movie.movietest.aop.Logged;
import com.test.movie.movietest.aop.LoggedExecutionTime;
import com.test.movie.movietest.cache.MovieSearchResult;
import com.test.movie.movietest.cache.MovieSearchResultRepository;
import com.test.movie.movietest.network.MovieDatabase;
import com.test.movie.movietest.network.omdb.OmdbService;
import com.test.movie.movietest.network.themoviedb.TMDbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    public static final Integer PAGE_SIZE = 20;

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    private static final List<String> VALID_OMDB_API_NAMES = List.of("omdb", "omdbapi", "omdb_api");

    private final OmdbService omdbService;
    private final TMDbService TMDbService;

    private final MovieSearchResultRepository movieSearchResultRepository;

    public MovieService(
            @Autowired OmdbService omdbService,
            @Autowired TMDbService TMDbService,
            @Autowired MovieSearchResultRepository movieSearchResultRepository
            ) {
        this.omdbService = omdbService;
        this.TMDbService = TMDbService;
        this.movieSearchResultRepository = movieSearchResultRepository;

        // TODO it is only for development
        movieSearchResultRepository.deleteAll();
    }

    @LoggedExecutionTime
    @Logged
    public Page<Movie> getMovies(String title, String apiName, String pageInp) {

        log.debug("title: {}", title);
        log.debug("apiName: {}", apiName);

        var pageNumber = parsePageNumber(pageInp);

        var key = title + "_" + apiName + "_" + pageNumber;
        var cachedSearch = movieSearchResultRepository.findById(key);

        if (cachedSearch.isEmpty()) {
            log.debug("No cachedSearch found for key: {}", key);
            final var movieDatabase = getMovieDatabase(apiName);

            var searchResult = movieDatabase.searhForMovies(title, pageNumber);

            var result = new PageImpl<>(
                    searchResult.stream().parallel().map(row -> new Movie(row.title(), row.year(), movieDatabase.getDirectors(row.movieId()))).toList(),
                    PageRequest.of(pageNumber, PAGE_SIZE),
                    searchResult.getTotalElements()
                    );
            movieSearchResultRepository.save(new MovieSearchResult(key, title, apiName, pageNumber, result.getTotalElements(), result.getContent()));
            return result;
        } else {
            log.debug("Found cachedSearch for key: {}", key);
            var cached = cachedSearch.get();
            return new PageImpl<>(cached.getMovies(), PageRequest.of(cached.getPage(), PAGE_SIZE), cached.getTotal());
        }
    }

    private int parsePageNumber(String pageNumberString) {
        var pageNum = 1;
        try {
            pageNum = Integer.parseInt(pageNumberString);
        } catch (NumberFormatException e) { /* do nothing */ }
        return pageNum;
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
