package com.test.movie.movietest.statistics;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchPatternRepository extends CrudRepository<SearchPattern, String> {
}
