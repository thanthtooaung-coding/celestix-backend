package com.movie.celestix.features.auth.service;

import com.movie.celestix.features.auth.dto.LoginResponse;
import com.movie.celestix.features.auth.dto.RegisterRequest;
import com.movie.celestix.features.auth.dto.UpdateMeRequest;
import com.movie.celestix.features.auth.dto.UserResponse;

public interface AuthService {
    LoginResponse authenticate(String email, String password);
    void register(RegisterRequest request);
    UserResponse getMe(String email);
    UserResponse updateMe(String email, UpdateMeRequest request);
}
