package com.movie.celestix.common.repository.jpa;

import com.movie.celestix.common.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
