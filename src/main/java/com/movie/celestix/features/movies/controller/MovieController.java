package com.movie.celestix.features.movies.controller;

import com.movie.celestix.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> retrieveOne(
            @PathVariable("id") Long id
    ) {
        return ApiResponse.ok("Movie: " + id, "Movie retrieved successfully");
    }
}
