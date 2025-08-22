package com.movie.celestix.features.showtimes.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ShowtimeResponse(
        Long id,
        MovieInfo movie,
        TheaterInfo theater,
        LocalDate showtimeDate,
        LocalTime showtimeTime,
        Integer seatsAvailable,
        String status
) {}
