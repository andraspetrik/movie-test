package com.test.movie.movietest.cache;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieSearchResultRepository extends CrudRepository<MovieSearchResult, String> {
}
