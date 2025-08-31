package com.movie.celestix.features.publicroutes;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.moviegenres.dto.MovieGenreResponse;
import com.movie.celestix.features.moviegenres.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/movie-genres")
@RequiredArgsConstructor
public class MovieGenrePublicController {

    private final MovieGenreService movieGenreService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovieGenreResponse>>> retrieveAll() {
        return ApiResponse.ok(this.movieGenreService.retrieveAll(), "Movie genres retrieved successfully");
    }
}
