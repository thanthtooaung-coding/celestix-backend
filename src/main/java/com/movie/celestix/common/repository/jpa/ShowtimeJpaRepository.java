package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeJpaRepository extends JpaRepository<Showtime, Long> {
}
