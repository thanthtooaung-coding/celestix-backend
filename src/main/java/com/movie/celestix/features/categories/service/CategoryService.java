package com.movie.celestix.features.categories.service;

import com.movie.celestix.features.categories.dto.CategoryResponse;
import com.movie.celestix.features.categories.dto.CreateCategoryRequest;
import com.movie.celestix.features.categories.dto.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest request);
    CategoryResponse retrieveOne(Long id);
    List<CategoryResponse> retrieveAll();
    CategoryResponse update(Long id, UpdateCategoryRequest request);
    void delete(Long id);
}
