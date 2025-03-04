package com.test.movie.movietest.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    MovieService movieService;

    public MovieController(@Autowired MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies/{movieTitle}")
    public GetMoviesResult getMovies(
            @PathVariable("movieTitle") String movieTitle,
            @RequestParam("apiName") String apiName) {
        var movies = movieService.getMovies(movieTitle, apiName);
        return new GetMoviesResult(movies);
    }
}
