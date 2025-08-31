package com.movie.celestix.features.email.dto;

import java.time.LocalTime;

public record ReminderEmailDetails(
        String customerName,
        String customerEmail,
        String movieTitle,
        String moviePosterUrl,
        String theaterName,
        LocalTime showtimeTime
) {}
