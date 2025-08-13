package com.movie.celestix.features.auth.service;

import com.movie.celestix.features.auth.dto.RegisterRequest;

public interface AuthService {
    String authenticate(String email, String password);
    void register(RegisterRequest request);
}
