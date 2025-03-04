package com.test.movie.movietest.model;

import java.util.List;

public record Movie(String title, String year, List<String> director) {
}
