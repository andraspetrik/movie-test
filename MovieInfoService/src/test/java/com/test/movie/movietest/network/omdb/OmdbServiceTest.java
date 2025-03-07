package com.test.movie.movietest.network.omdb;

import static org.junit.jupiter.api.Assertions.*;

import com.test.movie.movietest.network.NetworkConnector;
import com.test.movie.movietest.network.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OmdbServiceTest {

    @Mock
    private NetworkConnector networkConnector;

    @InjectMocks
    private OmdbService omdbService;

    private static final String API_KEY = "test-api-key";
    private static final String MOVIE_TITLE = "Inception";
    private static final int PAGE_NUMBER = 1;
    private static final String MOVIE_ID = "tt1375666";

    @BeforeEach
    void setUp() {
        omdbService = new OmdbService(API_KEY, networkConnector);
    }

    @Test
    void testSearchForMovies_Success() {
        // Mock API responses
        OmdbSearchResult response1 = new OmdbSearchResult(Optional.of(List.of(
                new OmdbSearchResultRow("Inception", "2010", "tt1375666")
        )), 100, true);

        OmdbSearchResult response2 = new OmdbSearchResult(Optional.of(List.of(
                new OmdbSearchResultRow("Inception 2", "2023", "tt1234567")
        )), 100, true);

        when(networkConnector.get(anyString(), eq(OmdbSearchResult.class)))
                .thenReturn(response1)
                .thenReturn(response2);

        // Execute
        Page<SearchResult> result = omdbService.searhForMovies(MOVIE_TITLE, PAGE_NUMBER);

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Inception", result.getContent().get(0).title());
        assertEquals("Inception 2", result.getContent().get(1).title());
        assertEquals(100, result.getTotalElements());

        verify(networkConnector, times(2)).get(anyString(), eq(OmdbSearchResult.class));
    }

    @Test
    void testSearchForMovies_EmptyResponse() {
        // Mock API responses with empty search results
        OmdbSearchResult emptyResponse = new OmdbSearchResult(Optional.empty(), 0, true);
        when(networkConnector.get(anyString(), eq(OmdbSearchResult.class))).thenReturn(emptyResponse);

        // Execute
        Page<SearchResult> result = omdbService.searhForMovies(MOVIE_TITLE, PAGE_NUMBER);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(networkConnector, times(2)).get(anyString(), eq(OmdbSearchResult.class));
    }

    @Test
    void testGetDirectors_Success() {
        // Mock API response
        OmdbDetailsResult response = new OmdbDetailsResult("Christopher Nolan");
        when(networkConnector.get(anyString(), eq(OmdbDetailsResult.class))).thenReturn(response);

        // Execute
        List<String> directors = omdbService.getDirectors(MOVIE_ID);

        // Assertions
        assertNotNull(directors);
        assertEquals(1, directors.size());
        assertEquals("Christopher Nolan", directors.get(0));

        verify(networkConnector, times(1)).get(anyString(), eq(OmdbDetailsResult.class));
    }

    @Test
    void testGetDirectors_MultipleDirectors() {
        // Mock API response
        OmdbDetailsResult response = new OmdbDetailsResult("Director One, Director Two");
        when(networkConnector.get(anyString(), eq(OmdbDetailsResult.class))).thenReturn(response);

        // Execute
        List<String> directors = omdbService.getDirectors(MOVIE_ID);

        // Assertions
        assertNotNull(directors);
        assertEquals(2, directors.size());
        assertEquals("Director One", directors.get(0));
        assertEquals("Director Two", directors.get(1));

        verify(networkConnector, times(1)).get(anyString(), eq(OmdbDetailsResult.class));
    }

    @Test
    void testGetDirectors_EmptyResponse() {
        // Mock API response with null
        when(networkConnector.get(anyString(), eq(OmdbDetailsResult.class))).thenReturn(null);

        // Execute
        List<String> directors = omdbService.getDirectors(MOVIE_ID);

        // Assertions
        assertNotNull(directors);
        assertTrue(directors.isEmpty());

        verify(networkConnector, times(1)).get(anyString(), eq(OmdbDetailsResult.class));
    }
}