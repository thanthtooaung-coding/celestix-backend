package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationJpaRepository extends JpaRepository<Configuration, Long> {
    Optional<Configuration> findByCode(String code);
}
