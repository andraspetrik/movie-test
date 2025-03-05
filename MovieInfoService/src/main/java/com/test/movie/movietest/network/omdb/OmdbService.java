package com.test.movie.movietest.network.omdb;

import com.test.movie.movietest.network.MovieDatabase;
import com.test.movie.movietest.network.NetworkConnector;
import com.test.movie.movietest.network.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class OmdbService implements MovieDatabase {

    private static final String BASE_URL = "http://omdbapi.com";

    private final String apiKey;
    private final NetworkConnector networkConnector;

    public OmdbService(@Value("${omdb.api-key}") String apiKey,
                       @Autowired NetworkConnector networkConnector) {
        this.apiKey = apiKey;
        this.networkConnector = networkConnector;
    }

    @Override
    public List<SearchResult> searhForMovies(String title) {
        // http://www.omdbapi.com/?s=Avengers&apikey=<<api key>>
        // Page size 10

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("s", title)
                .queryParam("apikey", apiKey)
                .toUriString();

        var response = networkConnector.get(url, OmdbSearchResult.class);

        var searchResult = response.search().orElse(List.of());

        return searchResult.stream()
                .map(row -> new SearchResult(row.title(), row.year(), row.imdbId()))
                .collect(Collectors.toList());
    }


    @Override
    public List<String> getDirectors(String movieId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
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
