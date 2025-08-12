package com.movie.celestix.features.movies.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @GetMapping("/{id}")
    public ResponseEntity<String> retrieveOne(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body("Movie: " + id);
    }
}
