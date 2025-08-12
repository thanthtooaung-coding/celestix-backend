package com.movie.celestix.features.moviegenres.controller;

import com.movie.celestix.features.moviegenres.dto.MovieGenreResponse;
import com.movie.celestix.features.moviegenres.dto.CreateMovieGenreRequest;
import com.movie.celestix.features.moviegenres.dto.UpdateMovieGenreRequest;
import com.movie.celestix.features.moviegenres.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movie-genres")
@RequiredArgsConstructor
public class MovieGenreController {

    private final MovieGenreService movieGenreService;

    @PostMapping
    public ResponseEntity<MovieGenreResponse> create(@RequestBody final CreateMovieGenreRequest request) {
        final MovieGenreResponse createdGenre = this.movieGenreService.create(request);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieGenreResponse> retrieveOne(@PathVariable final Long id) {
        return ResponseEntity.ok(this.movieGenreService.retrieveOne(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieGenreResponse>> retrieveAll() {
        return ResponseEntity.ok(this.movieGenreService.retrieveAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieGenreResponse> update(@PathVariable final Long id, @RequestBody final UpdateMovieGenreRequest request) {
        return ResponseEntity.ok(this.movieGenreService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        this.movieGenreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
