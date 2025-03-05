package com.test.movie.movietest.movie;

import com.test.movie.movietest.network.MovieDatabase;
import com.test.movie.movietest.network.MovieDetails;
import com.test.movie.movietest.network.omdb.OmdbService;
import com.test.movie.movietest.network.TheMovieDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    OmdbService omdbService;
    TheMovieDbService theMovieDbService;

    public MovieService(
            @Autowired OmdbService omdbService,
            @Autowired TheMovieDbService theMovieDbService
            ) {
        this.omdbService = omdbService;
        this.theMovieDbService = theMovieDbService;
    }

    public List<Movie> getMovies(String title, String apiName) {
        MovieDatabase movieDatabase = omdbService;
//        if (apiName.equals("omdbapi")) {
//
//        } else {
//            // If null, use it as default
//
//        }
        var searchResult = movieDatabase.searhForMovies(title);
        return searchResult.stream()
                .map(row -> {
                    var details = movieDatabase.getDetails(row.movieId());
                    var directors = details.orElse(MovieDetails.empty()).directors();
                    return new Movie(row.title(), row.year(), directors);
                })
                .toList();
    }
}
