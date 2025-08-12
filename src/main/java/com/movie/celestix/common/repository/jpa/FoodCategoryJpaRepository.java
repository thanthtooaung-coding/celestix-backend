package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryJpaRepository extends JpaRepository<FoodCategory, Long> {
}
