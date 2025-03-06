package com.test.movie.movietest.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatisticsService {

    private final SearchPatternRepository searchPatternRepository;

    public StatisticsService(@Autowired SearchPatternRepository searchPatternRepository) {
        this.searchPatternRepository = searchPatternRepository;
    }

    public void savePattern(String title, String apiName, Integer pageNumber) {
        var searchPattern = new SearchPattern();
        searchPattern.setTitle(title);
        searchPattern.setApiName(apiName);
        searchPattern.setPageNumber(pageNumber);
        searchPattern.setCreated(LocalDateTime.now());
        searchPatternRepository.save(searchPattern);
    }

}
