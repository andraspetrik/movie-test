package com.test.movie.movietest.network.themoviedb;

import com.test.movie.movietest.network.NetworkConnector;
import com.test.movie.movietest.network.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TMDbServiceTest {

    @Mock
    private NetworkConnector networkConnector;

    @InjectMocks
    private TMDbService tmDbService;

    private static final String API_KEY = "test-api-key";
    private static final String MOVIE_TITLE = "Inception";
    private static final int PAGE_NUMBER = 1;
    private static final String MOVIE_ID = "12345";

    @BeforeEach
    void setUp() {
        tmDbService = new TMDbService(API_KEY, networkConnector);
    }

    @Test
    void testSearchForMovies_Success() {
        // Mock API response
        TMDbSearchResult response = new TMDbSearchResult(
                1,
                List.of(
                new TMDbSearchResultRow("Inception", "tt1375666", LocalDate.of(2010, 7, 16) ),
                new TMDbSearchResultRow("Interstellar", "tt0816692", LocalDate.of(2014, 11, 7) )
        ), 5, 100);

        when(networkConnector.get(anyString(), eq(TMDbSearchResult.class))).thenReturn(response);

        // Execute
        Page<SearchResult> result = tmDbService.searhForMovies(MOVIE_TITLE, PAGE_NUMBER);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Inception", result.getContent().get(0).title());
        assertEquals("2010", result.getContent().get(0).year());
        assertEquals("Interstellar", result.getContent().get(1).title());
        assertEquals("2014", result.getContent().get(1).year());
        assertEquals(100, result.getTotalElements());

        verify(networkConnector, times(1)).get(anyString(), eq(TMDbSearchResult.class));
    }

    @Test
    void testSearchForMovies_EmptyResponse() {
        // Mock API response with no results
        TMDbSearchResult emptyResponse = new TMDbSearchResult(1, List.of(), 0, 0);
        when(networkConnector.get(anyString(), eq(TMDbSearchResult.class))).thenReturn(emptyResponse);

        // Execute
        Page<SearchResult> result = tmDbService.searhForMovies(MOVIE_TITLE, PAGE_NUMBER);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(networkConnector, times(1)).get(anyString(), eq(TMDbSearchResult.class));
    }

    @Test
    void testGetDirectors_Success() {
        // Mock API response
        TMDBCreditsResult response = new TMDBCreditsResult(List.of(
                new TMDBCredit("Director", "Christopher Nolan"),
                new TMDBCredit("Producer", "Emma Thomas")
        ));

        when(networkConnector.get(anyString(), eq(TMDBCreditsResult.class))).thenReturn(response);

        // Execute
        List<String> directors = tmDbService.getDirectors(MOVIE_ID);

        // Assertions
        assertNotNull(directors);
        assertEquals(1, directors.size());
        assertEquals("Christopher Nolan", directors.get(0));

        verify(networkConnector, times(1)).get(anyString(), eq(TMDBCreditsResult.class));
    }

    @Test
    void testGetDirectors_NoDirectors() {
        // Mock API response with no directors
        TMDBCreditsResult response = new TMDBCreditsResult(List.of(
                new TMDBCredit("Producer", "Emma Thomas")
        ));

        when(networkConnector.get(anyString(), eq(TMDBCreditsResult.class))).thenReturn(response);

        // Execute
        List<String> directors = tmDbService.getDirectors(MOVIE_ID);

        // Assertions
        assertNotNull(directors);
        assertTrue(directors.isEmpty());

        verify(networkConnector, times(1)).get(anyString(), eq(TMDBCreditsResult.class));
    }
}