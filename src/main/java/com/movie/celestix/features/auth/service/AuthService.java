package com.movie.celestix.features.auth.service;

import com.movie.celestix.features.auth.dto.RegisterRequest;

public interface AuthService {
    boolean authenticate(String email, String password);
    void register(RegisterRequest request);
}
