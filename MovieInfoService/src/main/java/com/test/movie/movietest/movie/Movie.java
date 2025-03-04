package com.test.movie.movietest.movie;

import java.util.List;

public record Movie(String title, String year, List<String> director) {
}
