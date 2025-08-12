package com.movie.celestix.features.moviegenres.service;

import com.movie.celestix.features.moviegenres.dto.MovieGenreResponse;
import com.movie.celestix.features.moviegenres.dto.CreateMovieGenreRequest;
import com.movie.celestix.features.moviegenres.dto.UpdateMovieGenreRequest;

import java.util.List;

public interface MovieGenreService {
    MovieGenreResponse create(CreateMovieGenreRequest request);
    MovieGenreResponse retrieveOne(Long id);
    List<MovieGenreResponse> retrieveAll();
    MovieGenreResponse update(Long id, UpdateMovieGenreRequest request);
    void delete(Long id);
}
