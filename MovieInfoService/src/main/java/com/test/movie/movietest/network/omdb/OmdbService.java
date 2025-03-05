package com.test.movie.movietest.network.omdb;

import com.test.movie.movietest.movie.MovieService;
import com.test.movie.movietest.network.MovieDatabase;
import com.test.movie.movietest.network.NetworkConnector;
import com.test.movie.movietest.network.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class OmdbService implements MovieDatabase {

    private static final String BASE_URL = "http://omdbapi.com";
    // private static final Integer PAGE_SIZE = 10;

    private final String apiKey;
    private final NetworkConnector networkConnector;

    public OmdbService(@Value("${omdb.api-key}") String apiKey,
                       @Autowired NetworkConnector networkConnector) {
        this.apiKey = apiKey;
        this.networkConnector = networkConnector;
    }

    @Override
    public Page<SearchResult> searhForMovies(String title, Integer pageNum) {


        var response1 = networkConnector.get(UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("s", title)
                .queryParam("apikey", apiKey)
                .queryParam("page", pageNum * 2 - 1)
                .toUriString(), OmdbSearchResult.class);

        var response2 = networkConnector.get(UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("s", title)
                .queryParam("apikey", apiKey)
                .queryParam("page", pageNum * 2)
                .toUriString(), OmdbSearchResult.class);

        var searchResult1 = response1.search().orElse(List.of());
        var searchResult2 = response2.search().orElse(List.of());
        var searchResult = Stream.of(searchResult1, searchResult2)
                .flatMap(Collection::stream).toList();

        return new PageImpl<>(
                searchResult.stream()
                        .map(row -> new SearchResult(row.title(), row.year(), row.imdbId()))
                        .collect(Collectors.toList()),
        PageRequest.of(pageNum, MovieService.PAGE_SIZE),
        response1.totalResults()
        );
    }

    @Override
    public List<String> getDirectors(String movieId) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("i", movieId)
                .queryParam("apikey", apiKey)
                .toUriString();

       var response = networkConnector.get(url, OmdbDetailsResult.class);

        if (response == null) {
            return List.of();
        }

        var directorString = response.director();
        var directors = directorString.split(", ");

        return Arrays.stream(directors).toList();
    }
}
