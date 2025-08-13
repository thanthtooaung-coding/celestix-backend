package com.movie.celestix.common.config;

import com.movie.celestix.common.enums.Role;
import com.movie.celestix.common.repository.jpa.UserJpaRepository;
import com.movie.celestix.features.auth.dto.RegisterRequest;
import com.movie.celestix.features.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final AuthService authService;
    private final UserJpaRepository userJpaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (this.userJpaRepository.findByEmail("admin@celestix.com").isEmpty()) {
            final RegisterRequest admin = new RegisterRequest(
                    "Admin",
                    "admin@celestix.com",
                    "admin123",
                    Role.ADMIN
            );
            this.authService.register(admin);
        }
    }
}
