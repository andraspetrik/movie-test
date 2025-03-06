package com.test.movie.movietest.statistics;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "search_pattern")
@NoArgsConstructor @Data @AllArgsConstructor @ToString
public class SearchPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private LocalDateTime created;

    @Column
    private String title;

    @Column(name = "api_name")
    private String apiName;

    @Column(name = "page_number")
    private Integer pageNumber;

}
