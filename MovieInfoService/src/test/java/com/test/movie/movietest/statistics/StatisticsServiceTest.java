package com.test.movie.movietest.statistics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private SearchPatternRepository searchPatternRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void testSavePattern() {
        // Given
        String title = "Test Movie";
        String apiName = "TestAPI";
        int pageNumber = 1;

        // When
        statisticsService.savePattern(title, apiName, pageNumber);

        // Then
        verify(searchPatternRepository, timeout(1000)).save(any(SearchPattern.class));
    }
}