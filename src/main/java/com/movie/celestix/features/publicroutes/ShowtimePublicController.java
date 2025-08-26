package com.movie.celestix.features.publicroutes;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.showtimes.dto.GroupedShowtimeResponse;
import com.movie.celestix.features.showtimes.dto.ShowtimeResponse;
import com.movie.celestix.features.showtimes.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/showtimes")
@RequiredArgsConstructor
public class ShowtimePublicController {
    private final ShowtimeService showtimeService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowtimeResponse>> retrieveOne(@PathVariable Long id) {
        return ApiResponse.ok(showtimeService.retrieveOne(id), "Showtime retrieved successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShowtimeResponse>>> retrieveAll() {
        return ApiResponse.ok(showtimeService.retrieveAll(), "Showtimes retrieved successfully");
    }

    @GetMapping("/grouped")
    public ResponseEntity<ApiResponse<List<GroupedShowtimeResponse>>> retrieveAllGroupByMovieAndTheater() {
        return ApiResponse.ok(showtimeService.retrieveAllGroupByMovieAndTheater(),
                "Showtimes grouped by movie and theater retrieved successfully");
    }
}