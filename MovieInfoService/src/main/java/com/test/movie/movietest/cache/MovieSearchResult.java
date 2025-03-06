package com.test.movie.movietest.cache;

import com.test.movie.movietest.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@RedisHash(value = "MovieSearchResult", timeToLive = 3600L)
@NoArgsConstructor @Data @AllArgsConstructor @ToString
public class MovieSearchResult implements Serializable {

    @Id
    private String id;

    private String title;

    private String apiName;

    private Integer page;

    private Long total;

    private List<Movie> movies;

}
