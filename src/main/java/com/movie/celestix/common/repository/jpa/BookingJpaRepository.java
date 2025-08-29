package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingJpaRepository extends JpaRepository<Booking, Long> {
}
