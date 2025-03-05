package com.test.movie.movietest.network.themoviedb;

import com.test.movie.movietest.aop.Logged;
import com.test.movie.movietest.network.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TMDbService implements MovieDatabase {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final Integer PAGE_SIZE = 20;

    private final String apiKey;
    private final NetworkConnector networkConnector;

    public TMDbService(@Value("${themoviedb.api-key}") String apiKey,
                       @Autowired NetworkConnector networkConnector) {
        this.apiKey = apiKey;
        this.networkConnector = networkConnector;
    }

    @Override
    public Page<SearchResult> searhForMovies(String title, Integer pageNumber) {
        // https://api.themoviedb.org/3/search/movie?api_key=<<api key>>&query=Avengers&include_adult=true

        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/search/movie")
                .queryParam("query", title)
                .queryParam("api_key", apiKey)
                .queryParam("include_adult", "true")
                .toUriString();

        var response = networkConnector.get(url, TMDbSearchResult.class);

        var searchResult = response.results();

        return new PageImpl<>(
                searchResult.stream()
                        .map(row -> new SearchResult(row.title(), row.releaseDate().getYear() + "", row.id()))
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, PAGE_SIZE),
                response.totalResults()
        );
    }

    @Logged
    @Override
    public List<String> getDirectors(String movieId) {

        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/movie/" + movieId + "/credits?")
                .queryParam("api_key", apiKey)
                .toUriString();

        var response = networkConnector.get(url, TMDBCreditsResult.class);

        return response.crew().stream()
                .filter(c -> c.job().equals("Director"))
                .map(TMDBCredit::name)
                .toList();
    }


}
