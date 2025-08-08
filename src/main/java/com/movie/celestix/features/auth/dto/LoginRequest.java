package com.movie.celestix.features.auth.dto;

public record LoginRequest(
    String email,
    String password
) {}
