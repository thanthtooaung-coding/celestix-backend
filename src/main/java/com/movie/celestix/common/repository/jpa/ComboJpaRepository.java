package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboJpaRepository extends JpaRepository<Combo, Long> {
    boolean existsByFoods_Id(Long foodId);
}
