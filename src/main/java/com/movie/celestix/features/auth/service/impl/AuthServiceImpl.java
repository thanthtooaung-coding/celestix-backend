package com.movie.celestix.features.auth.service.impl;

import com.movie.celestix.common.models.User;
import com.movie.celestix.common.repository.jdbc.UserJdbcRepository;
import com.movie.celestix.common.repository.jpa.UserJpaRepository;
import com.movie.celestix.features.auth.dto.RegisterRequest;
import com.movie.celestix.features.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserJdbcRepository userJdbcRepository;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean authenticate(final String email, final String rawPassword) {
        final String storedHashedPassword = this.userJdbcRepository.findPasswordByEmail(email);
        return storedHashedPassword != null && this.passwordEncoder.matches(rawPassword, storedHashedPassword);
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
                        hashedPassword,
                        request.email(),
                        request.phoneNumber(),
                        request.role()
                )
        );
    }
}
