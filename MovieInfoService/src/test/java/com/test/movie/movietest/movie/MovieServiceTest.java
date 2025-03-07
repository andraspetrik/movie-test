package com.test.movie.movietest.movie;

import static org.junit.jupiter.api.Assertions.*;

import com.test.movie.movietest.cache.MovieSearchResult;
import com.test.movie.movietest.cache.MovieSearchResultRepository;
import com.test.movie.movietest.network.SearchResult;
import com.test.movie.movietest.network.omdb.OmdbService;
import com.test.movie.movietest.network.themoviedb.TMDbService;
import com.test.movie.movietest.statistics.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private OmdbService omdbService;

    @Mock
    private TMDbService tmDbService;

    @Mock
    private StatisticsService statisticsService;

    @Mock
    private MovieSearchResultRepository movieSearchResultRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        // Clear all repository data in constructor (Mock behavior)
//        doNothing().when(movieSearchResultRepository).deleteAll();
    }

    @Test
    void testGetMovies_Omdb_Cached() {
        // Given
        String title = "Test Movie";
        String apiName = "omdb";
        String pageInp = "1";
        String key = title + "_" + apiName + "_1";

        List<Movie> movies = List.of(new Movie("Test Movie", "2024", List.of("Director A")));

        MovieSearchResult cachedResult = new MovieSearchResult(key, title, apiName, 1, (long) movies.size(), movies);
        when(movieSearchResultRepository.findById(key)).thenReturn(Optional.of(cachedResult));

        // When
        Page<Movie> result = movieService.getMovies(title, apiName, pageInp);

        // Then
        assertNotNull(result);
        assertEquals("Test Movie", result.getContent().getFirst().title());
        verify(movieSearchResultRepository, times(1)).findById(key);
        verifyNoInteractions(omdbService, tmDbService);
    }

    @Test
    void testGetMovies_OMDb_NotCached() {
        // Given
        String title = "New Movie";
        String apiName = "omdb";
        String pageInp = "1";
        String key = title + "_" + apiName + "_1";

        when(movieSearchResultRepository.findById(key)).thenReturn(Optional.empty());
        when(omdbService.searhForMovies(title, 1)).thenReturn(
               new PageImpl<>(List.of(new SearchResult("New Movie", "2024", "id1")))
        );
        when(omdbService.getDirectors("id1")).thenReturn(List.of("Director B"));

        // When
        Page<Movie> result = movieService.getMovies(title, apiName, pageInp);

        // Then
        assertNotNull(result);
        assertEquals("New Movie", result.getContent().getFirst().title());
        verify(movieSearchResultRepository, times(1)).findById(key);
        verify(omdbService, times(1)).searhForMovies(title, 1);
        verify(omdbService, times(1)).getDirectors("id1");
        verify(movieSearchResultRepository, times(1)).save(any(MovieSearchResult.class));
    }

    @Test
    void testGetMovies_NoPageNumber() {
        // Given
        String title = "Test Movie";
        String apiName = "omdb";
        String pageInp = "invalid";

        when(movieSearchResultRepository.findById(any())).thenReturn(Optional.empty());
        when(omdbService.searhForMovies(title, 1)).thenReturn(
                new PageImpl<>(List.of(new SearchResult("Test Movie", "2024", "id1")))
        );

        // When
        Page<Movie> result = movieService.getMovies(title, apiName, pageInp);

        // Then
        assertNotNull(result);
        verify(omdbService, times(1)).searhForMovies(title, 1);
    }
}