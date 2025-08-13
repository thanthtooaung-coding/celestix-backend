package com.movie.celestix.features.auth.service.impl;

import com.movie.celestix.common.jwt.JwtUtil;
import com.movie.celestix.common.models.User;
import com.movie.celestix.common.repository.jdbc.UserJdbcRepository;
import com.movie.celestix.common.repository.jpa.UserJpaRepository;
import com.movie.celestix.features.auth.dto.RegisterRequest;
import com.movie.celestix.features.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserJdbcRepository userJdbcRepository;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public String authenticate(final String email, final String rawPassword) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, rawPassword));
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        return this.jwtUtil.generateToken(userDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(final RegisterRequest request) {
        if (this.userJdbcRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already registered!");
        }

        final String hashedPassword = this.passwordEncoder.encode(request.password());

        this.userJpaRepository.save(
                new User(
                        request.name(),
                        hashedPassword,
                        request.email(),
                        request.role()
                )
        );
    }
}
