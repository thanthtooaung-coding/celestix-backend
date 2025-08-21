package com.movie.celestix.features.theater.service;

import com.movie.celestix.features.theater.dto.CreateTheaterRequest;
import com.movie.celestix.features.theater.dto.TheaterResponse;
import com.movie.celestix.features.theater.dto.UpdateTheaterRequest;

import java.util.List;

public interface TheaterService {
    TheaterResponse create(CreateTheaterRequest request);
    TheaterResponse retrieveOne(Long id);
    List<TheaterResponse> retrieveAll();
    TheaterResponse update(Long id, UpdateTheaterRequest request);
    void delete(Long id);
}
