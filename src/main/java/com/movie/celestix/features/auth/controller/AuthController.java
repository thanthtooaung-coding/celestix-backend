package com.movie.celestix.features.auth.controller;

import com.movie.celestix.features.auth.dto.LoginRequest;
import com.movie.celestix.features.auth.dto.RegisterRequest;
import com.movie.celestix.features.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody final LoginRequest request) {
        boolean success = this.authService.authenticate(request.email(), request.password());
        if (success) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody final RegisterRequest request) {
        this.authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }
}
