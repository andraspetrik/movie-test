package com.test.movie.movietest.rest.v1;

import com.test.movie.movietest.model.GetMoviesResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("/")
public class MovieController {

    @GetMapping("/movies/{movieTitle}")
    public GetMoviesResult getMovies(
            @PathVariable("movieTitle") String movieTitle,
            @RequestParam("apiName") String apiName) {
        return new GetMoviesResult(List.of());
    }
}
