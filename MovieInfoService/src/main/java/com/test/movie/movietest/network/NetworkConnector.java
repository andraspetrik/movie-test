package com.test.movie.movietest.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class NetworkConnector {

    private static final Logger log = LoggerFactory.getLogger(NetworkConnector.class);

    private final RestTemplate restTemplate;

    public NetworkConnector(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <R> R get(String url, Class<R> responseType) {
        var response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<Void>(null, new HttpHeaders()),
                responseType);

        log.debug("Status: {}", response.getStatusCode());

        if (response.getStatusCode() != HttpStatus.OK) {
            log.debug("Error: {}", response.getStatusCode());
            throw new NetworkException("Can not reach external API!");
        }

        var body = response.getBody();

        if (body == null) {
            throw new NetworkException("No response from external API!");
        }

        return body;
    }
}
