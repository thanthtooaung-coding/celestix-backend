package com.movie.celestix.features.showtimes.service;

import com.movie.celestix.features.showtimes.dto.CreateShowtimeRequest;
import com.movie.celestix.features.showtimes.dto.ShowtimeResponse;
import com.movie.celestix.features.showtimes.dto.ShowtimeTemplateResponse;
import com.movie.celestix.features.showtimes.dto.UpdateShowtimeRequest;

import java.util.List;

public interface ShowtimeService {
    ShowtimeResponse create(CreateShowtimeRequest request);
    ShowtimeResponse retrieveOne(Long id);
    List<ShowtimeResponse> retrieveAll();
    ShowtimeResponse update(Long id, UpdateShowtimeRequest request);
    void delete(Long id);
    ShowtimeTemplateResponse getShowtimeTemplate();
}
