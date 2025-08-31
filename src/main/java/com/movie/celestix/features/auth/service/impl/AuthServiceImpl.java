package com.movie.celestix.features.auth.service.impl;

import com.movie.celestix.common.jwt.JwtUtil;
import com.movie.celestix.common.models.User;
import com.movie.celestix.common.repository.jdbc.UserJdbcRepository;
import com.movie.celestix.common.repository.jpa.UserJpaRepository;
import com.movie.celestix.features.auth.dto.LoginResponse;
import com.movie.celestix.features.auth.dto.RegisterRequest;
import com.movie.celestix.features.auth.dto.UpdateMeRequest;
import com.movie.celestix.features.auth.dto.UserResponse;
import com.movie.celestix.features.auth.service.AuthService;
import com.movie.celestix.features.booking.dto.MyBookingsResponse;
import com.movie.celestix.features.booking.service.BookingService;
import com.movie.celestix.features.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserJdbcRepository userJdbcRepository;
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final MediaService mediaService;
    private final BookingService bookingService;

    @Override
    public LoginResponse authenticate(final String email, final String rawPassword) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, rawPassword));
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        final User user = this.userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        final String token = this.jwtUtil.generateToken(userDetails);
        return new LoginResponse(token, user.getRole());
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
                        request.role(),
                        null
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMe(String email) {
        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getProfileUrl());
    }

    @Override
    @Transactional
    public UserResponse updateMe(String email, UpdateMeRequest request) {
        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (request.name() != null) {
            user.setName(request.name());
        }

        User updatedUser = userJpaRepository.save(user);
        return new UserResponse(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getRole(), updatedUser.getProfileUrl());
    }

    @Transactional
    @Override
    public UserResponse updateProfilePicture(String email, MultipartFile file) {
        final User user = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        final String profileUrl = mediaService.store(file);
        user.setProfileUrl(profileUrl);
        final User updatedUser = userJpaRepository.save(user);
        return new UserResponse(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getRole(), updatedUser.getProfileUrl());
    }

    @Override
    public MyBookingsResponse getMyBookings(String email) {
        return bookingService.retrieveMyBookings(email);
    }
}
