package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowtimeJpaRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByShowtimeDateAndShowtimeTimeBetween(LocalDate date, LocalTime start, LocalTime end);
}
