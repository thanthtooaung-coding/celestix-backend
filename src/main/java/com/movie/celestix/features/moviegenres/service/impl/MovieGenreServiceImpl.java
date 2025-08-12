package com.movie.celestix.features.moviegenres.service.impl;

import com.movie.celestix.common.models.MovieGenre;
import com.movie.celestix.common.repository.jpa.MovieGenreJpaRepository;
import com.movie.celestix.features.moviegenres.dto.MovieGenreResponse;
import com.movie.celestix.features.moviegenres.dto.CreateMovieGenreRequest;
import com.movie.celestix.features.moviegenres.dto.UpdateMovieGenreRequest;
import com.movie.celestix.features.moviegenres.mapper.MovieGenreMapper;
import com.movie.celestix.features.moviegenres.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieGenreServiceImpl implements MovieGenreService {

    private final MovieGenreJpaRepository movieGenreJpaRepository;
    private final MovieGenreMapper movieGenreMapper;

    @Override
    @Transactional
    public MovieGenreResponse create(CreateMovieGenreRequest request) {
        final MovieGenre movieGenre = new MovieGenre(request.name(), request.description());
        final MovieGenre savedMovieGenre = this.movieGenreJpaRepository.save(movieGenre);
        return this.movieGenreMapper.toDto(savedMovieGenre);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieGenreResponse retrieveOne(Long id) {
        final MovieGenre movieGenre = this.movieGenreJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre with id " + id + " not found"));
        return this.movieGenreMapper.toDto(movieGenre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieGenreResponse> retrieveAll() {
        final List<MovieGenre> genres = movieGenreJpaRepository.findAll();
        return this.movieGenreMapper.toDtoList(genres);
    }

    @Override
    @Transactional
    public MovieGenreResponse update(Long id, UpdateMovieGenreRequest request) {
        final MovieGenre movieGenre = this.movieGenreJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Genre with id " + id + " not found"));
        movieGenre.setName(request.name());
        movieGenre.setDescription(request.description());
        final MovieGenre updatedMovieGenre = this.movieGenreJpaRepository.save(movieGenre);
        return this.movieGenreMapper.toDto(updatedMovieGenre);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!movieGenreJpaRepository.existsById(id)) {
            throw new RuntimeException("Genre with id " + id + " not found");
        }
        this.movieGenreJpaRepository.deleteById(id);
    }
}
