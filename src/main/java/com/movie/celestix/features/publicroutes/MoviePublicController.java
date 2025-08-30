package com.movie.celestix.features.publicroutes;

import com.cloudinary.utils.StringUtils;
import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.movies.dto.MovieResponse;
import com.movie.celestix.features.movies.service.MovieService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/movies")
@RequiredArgsConstructor
public class MoviePublicController {
    private final MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> retrieveOne(@PathVariable final Long id) {
        return ApiResponse.ok(movieService.retrieveOne(id), "Movie retrieved successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovieResponse>>> retrieveAll(
            @Parameter(
                    description = "Filter movies by status",
                    schema = @Schema(allowableValues = {"Now Showing", "Coming Soon", "all"})
            )
            @RequestParam(required = false) String status
    ) {
        List<MovieResponse> movieResponseList;
        if (StringUtils.isBlank(status) || status.equalsIgnoreCase("all") ) {
            movieResponseList = movieService.retrieveAvailableMovies();
        } else {
            movieResponseList = movieService.retrieveAllAvailableMoviesByStatus(status);
        }
        return ApiResponse.ok(
                movieResponseList
                , "Movies retrieved successfully");
    }
}
