package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGenreJpaRepository extends JpaRepository<MovieGenre, Long> {
}
