package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedSeatJpaRepository extends JpaRepository<BookedSeat, Long> {
    List<BookedSeat> findByShowtimeId(Long showtimeId);
}
