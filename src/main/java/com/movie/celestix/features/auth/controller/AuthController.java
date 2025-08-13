package com.movie.celestix.features.auth.controller;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.auth.dto.LoginRequest;
import com.movie.celestix.features.auth.dto.RegisterRequest;
import com.movie.celestix.features.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody final LoginRequest request) {
        boolean success = this.authService.authenticate(request.email(), request.password());
        if (success) {
            return ApiResponse.ok(null, "Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<String>builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message("Invalid email or password")
                            .build()
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody final RegisterRequest request) {
        this.authService.register(request);
        return ApiResponse.created(null, "User registered successfully");
    }
}
