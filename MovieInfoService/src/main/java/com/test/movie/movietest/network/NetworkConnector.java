package com.test.movie.movietest.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class NetworkConnector {

    private final RestTemplate restTemplate;

    public NetworkConnector(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <R> R get(String url, Class<R> responseType) {
        var response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<Void>(null, new HttpHeaders()),
                responseType);

        System.out.println("Status: " + response.getStatusCode());

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Error: " + response.getStatusCode());
//            throw new Exception("Can not reach external API!");
            // TODO Handle this
        }

        System.out.println(response); // TODO remove it

        var body = response.getBody();

        if (body == null) {
            // TODO Handle this
        }

        return body;
    }
}
