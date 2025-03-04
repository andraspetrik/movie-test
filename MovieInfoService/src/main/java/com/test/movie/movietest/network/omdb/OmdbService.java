package com.test.movie.movietest.network.omdb;

import com.test.movie.movietest.network.MovieCredits;
import com.test.movie.movietest.network.MovieDatabase;
import com.test.movie.movietest.network.MovieDetails;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OmdbService implements MovieDatabase {

    private static final String BASE_URL = "http://omdbapi.com";

    private final String apiKey;
    private final RestTemplate restTemplate;

    public OmdbService(@Value("${omdb.api-key}") String apiKey,
                       @Autowired RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<SearchResult> searhForMovies(String title) {
        // http://www.omdbapi.com/?s=Avengers&apikey=<<api key>>

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("s", title)
                .queryParam("apikey", apiKey)
                .toUriString();

        var headers = new HttpHeaders();

        var response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<Void>(null, headers),
                OmdbSearchResult.class
        );

        System.out.println("Status: " + response.getStatusCode());

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Error: " + response.getStatusCode());
//            throw new Exception("Can not reach external API!");
        }

        System.out.println(response);


        var searchResult = Objects.requireNonNull(response.getBody()).search().orElse(List.of());

        return searchResult.stream()
                .map(row -> new SearchResult(row.title(), row.year(), row.imdbId()))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<MovieDetails> getDetails(String movieId) {
        return Optional.empty();
    }

    @Override
    public Optional<MovieCredits> getCredits(String creditId) {
        return Optional.empty();
    }
}
