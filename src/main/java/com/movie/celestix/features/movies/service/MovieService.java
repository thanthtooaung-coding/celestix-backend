package com.movie.celestix.features.movies.service;

import com.movie.celestix.features.movies.dto.CreateMovieRequest;
import com.movie.celestix.features.movies.dto.MovieResponse;
import com.movie.celestix.features.movies.dto.MovieTemplateResponse;
import com.movie.celestix.features.movies.dto.UpdateMovieRequest;
import com.movie.celestix.features.publicroutes.dto.PopularMovieResponse;

import java.util.List;

public interface MovieService {
    MovieResponse create(CreateMovieRequest request);
    MovieResponse retrieveOne(Long id);
    List<MovieResponse> retrieveAll();
    MovieResponse update(Long id, UpdateMovieRequest request);
    void delete(Long id);
    MovieTemplateResponse getMovieTemplate();
    List<MovieResponse> retrieveAllByStatus(String status);
    List<MovieResponse> retrieveAvailableMovies();
    List<MovieResponse> retrieveAllAvailableMoviesByStatus(String status);
    List<PopularMovieResponse> retrievePopularMovies();
}
