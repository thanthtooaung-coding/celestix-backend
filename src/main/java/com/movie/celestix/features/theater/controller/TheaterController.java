package com.movie.celestix.features.theater.controller;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.theater.dto.CreateTheaterRequest;
import com.movie.celestix.features.theater.dto.TheaterResponse;
import com.movie.celestix.features.theater.dto.UpdateTheaterRequest;
import com.movie.celestix.features.theater.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<ApiResponse<TheaterResponse>> create(@RequestBody final CreateTheaterRequest request) {
        final TheaterResponse createdTheater = theaterService.create(request);
        return ApiResponse.created(createdTheater, "Theater created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TheaterResponse>> retrieveOne(@PathVariable final Long id) {
        return ApiResponse.ok(theaterService.retrieveOne(id), "Theater retrieved successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TheaterResponse>>> retrieveAll() {
        return ApiResponse.ok(theaterService.retrieveAll(), "Theaters retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TheaterResponse>> update(
            @PathVariable final Long id,
            @RequestBody final UpdateTheaterRequest request
    ) {
        return ApiResponse.ok(theaterService.update(id, request), "Theater updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable final Long id) {
        theaterService.delete(id);
        return ApiResponse.noContent("Theater deleted successfully");
    }
}
