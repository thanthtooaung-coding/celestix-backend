package com.movie.celestix.features.movies.service.impl;

import com.movie.celestix.common.enums.MovieLanguage;
import com.movie.celestix.common.enums.MovieRating;
import com.movie.celestix.common.enums.MovieStatus;
import com.movie.celestix.common.models.Movie;
import com.movie.celestix.common.models.MovieGenre;
import com.movie.celestix.common.repository.jpa.MovieGenreJpaRepository;
import com.movie.celestix.common.repository.jpa.MovieJpaRepository;
import com.movie.celestix.features.movies.dto.CreateMovieRequest;
import com.movie.celestix.features.movies.dto.MovieResponse;
import com.movie.celestix.features.movies.dto.MovieTemplateResponse;
import com.movie.celestix.features.movies.dto.UpdateMovieRequest;
import com.movie.celestix.features.movies.mapper.MovieMapper;
import com.movie.celestix.features.movies.service.MovieEnumService;
import com.movie.celestix.features.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieJpaRepository movieJpaRepository;
    private final MovieGenreJpaRepository movieGenreJpaRepository;
    private final MovieMapper movieMapper;
    private final MovieEnumService movieEnumService;


    @Override
    @Transactional
    public MovieResponse create(CreateMovieRequest request) {
        final Movie movie = movieMapper.toEntity(request);
        movie.setRating(getRatingFromString(request.rating()));
        movie.setLanguage(getLanguageFromString(request.language()));
        movie.setStatus(getStatusFromString(request.status()));
        return getMovieResponse(movie, request.genreIds());
    }

    private MovieResponse getMovieResponse(final Movie movie, final Set<Long> genresIds) {
        final Set<MovieGenre> genres = new HashSet<>(movieGenreJpaRepository.findAllById(genresIds));
        if (genres.size() != genresIds.size()) {
            throw new RuntimeException("One or more genres not found");
        }
        movie.setGenres(genres);
        final Movie savedMovie = movieJpaRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse retrieveOne(Long id) {
        final Movie movie = movieJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie with id " + id + " not found"));
        return movieMapper.toDto(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> retrieveAll() {
        return movieJpaRepository.findAll().stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MovieResponse update(Long id, UpdateMovieRequest request) {
        final Movie movie = movieJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie with id " + id + " not found"));
        movieMapper.updateMovieFromDto(request, movie);

        if (request.rating() != null) {
            movie.setRating(getRatingFromString(request.rating()));
        }
        if (request.language() != null) {
            movie.setLanguage(getLanguageFromString(request.language()));
        }
        if (request.status() != null) {
            movie.setStatus(getStatusFromString(request.status()));
        }

        return getMovieResponse(movie, request.genreIds());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!movieJpaRepository.existsById(id)) {
            throw new RuntimeException("Movie with id " + id + " not found");
        }
        movieJpaRepository.deleteById(id);
    }

    @Override
    public MovieTemplateResponse getMovieTemplate() {
        return new MovieTemplateResponse(
                movieEnumService.getAllRatings(),
                movieEnumService.getAllLanguages(),
                movieEnumService.getAllStatuses()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> retrieveAllByStatus(String status) {
        MovieStatus movieStatus = getStatusFromString(status);
        return movieJpaRepository.findAllByStatus(movieStatus).stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    private MovieRating getRatingFromString(String ratingString) {
        return Arrays.stream(MovieRating.values())
                .filter(r -> r.getDisplayName().equalsIgnoreCase(ratingString))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid MovieRating: " + ratingString));
    }

    private MovieLanguage getLanguageFromString(String languageString) {
        return Arrays.stream(MovieLanguage.values())
                .filter(l -> l.getDisplayName().equalsIgnoreCase(languageString))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid MovieLanguage: " + languageString));
    }

    private MovieStatus getStatusFromString(String statusString) {
        return Arrays.stream(MovieStatus.values())
                .filter(s -> s.getDisplayName().equalsIgnoreCase(statusString))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid MovieStatus: " + statusString));
    }
}
