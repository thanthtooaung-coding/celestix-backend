package com.movie.celestix.features.dashboard.service.impl;

import com.movie.celestix.common.enums.Role;
import com.movie.celestix.common.repository.jpa.BookingJpaRepository;
import com.movie.celestix.common.repository.jpa.MovieJpaRepository;
import com.movie.celestix.common.repository.jpa.TheaterJpaRepository;
import com.movie.celestix.common.repository.jpa.UserJpaRepository;
import com.movie.celestix.features.dashboard.dto.DashboardStatsResponse;
import com.movie.celestix.features.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserJpaRepository userJpaRepository;
    private final TheaterJpaRepository theaterJpaRepository;
    private final MovieJpaRepository movieJpaRepository;
    private final BookingJpaRepository bookingJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userJpaRepository.count();
        long totalCinemas = theaterJpaRepository.count();
        long totalMovies = movieJpaRepository.count();
        long totalReservations = bookingJpaRepository.count();

        long totalAdmins = userJpaRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.ADMIN)
                .count();

        return new DashboardStatsResponse(
                totalUsers,
                totalCinemas,
                totalMovies,
                totalAdmins,
                totalReservations
        );
    }
}
