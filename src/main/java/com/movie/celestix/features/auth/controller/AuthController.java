package com.movie.celestix.features.auth.controller;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.auth.dto.*;
import com.movie.celestix.features.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody final LoginRequest request) {
        try {
            final LoginResponse loginResponse = this.authService.authenticate(request.email(), request.password());
            return ApiResponse.ok(loginResponse, "Login successful");
        } catch (BadCredentialsException e) {
            return ApiResponse.unauthorized("Incorrect email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody final RegisterRequest request) {
        this.authService.register(request);
        return ApiResponse.created(null, "User registered successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(authService.getMe(userDetails.getUsername()), "User retrieved successfully");
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateMeRequest request
    ) {
        return ApiResponse.ok(authService.updateMe(userDetails.getUsername(), request), "User updated successfully");
    }
}
