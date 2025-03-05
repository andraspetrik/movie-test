package com.test.movie.movietest.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(@Autowired MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies/{movieTitle}")
    public GetMoviesResult getMovies(
            @PathVariable("movieTitle") String movieTitle,
            @RequestParam(value = "apiName", required = false) String apiName,
            @RequestParam(value = "page", required = false) String page
    ) {
        var movies = movieService.getMovies(movieTitle, apiName, page);
        return new GetMoviesResult(movies);
    }
}
