package com.test.movie.movietest.network.themoviedb;

import com.test.movie.movietest.aop.Logged;
import com.test.movie.movietest.network.*;
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
public class TMDbService implements MovieDatabase {

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    private final String apiKey;
    private final NetworkConnector networkConnector;

    public TMDbService(@Value("${themoviedb.api-key}") String apiKey,
                       @Autowired NetworkConnector networkConnector) {
        this.apiKey = apiKey;
        this.networkConnector = networkConnector;
    }

    @Override
    public List<SearchResult> searhForMovies(String title) {
        // https://api.themoviedb.org/3/search/movie?api_key=<<api key>>&query=Avengers&include_adult=true

        // Page size 20

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/search/movie")
                .queryParam("query", title)
                .queryParam("api_key", apiKey)
                .queryParam("include_adult", "true")
                .toUriString();

        var response = networkConnector.get(url, TMDbSearchResult.class);

        var searchResult = response.results();

        return searchResult.stream()
                .map(row -> new SearchResult(row.title(), row.releaseDate().getYear() + "", row.id()))
                .collect(Collectors.toList());
//        return List.of();
    }

    @Logged
    @Override
    public List<String> getDirectors(String movieId) {

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/movie/" + movieId + "/credits?")
                .queryParam("api_key", apiKey)
                .toUriString();

        var response = networkConnector.get(url, TMDBCreditsResult.class);

        var directors = response.crew().stream()
                .filter(c -> c.job().equals("Director"))
                .map(TMDBCredit::name)
                .toList();

        return directors;

//        return List.of();
    }


}
