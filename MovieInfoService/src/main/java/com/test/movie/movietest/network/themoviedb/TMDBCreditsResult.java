package com.test.movie.movietest.network.themoviedb;

import java.util.List;

public record TMDBCreditsResult(
        List<TMDBCredit> crew
) {
}
