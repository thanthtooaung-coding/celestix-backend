package com.movie.celestix.features.foodcategories.service;

import com.movie.celestix.features.foodcategories.dto.FoodCategoryResponse;
import com.movie.celestix.features.foodcategories.dto.CreateFoodCategoryRequest;
import com.movie.celestix.features.foodcategories.dto.UpdateFoodCategoryRequest;

import java.util.List;

public interface FoodCategoryService {
    FoodCategoryResponse create(CreateFoodCategoryRequest request);
    FoodCategoryResponse retrieveOne(Long id);
    List<FoodCategoryResponse> retrieveAll();
    FoodCategoryResponse update(Long id, UpdateFoodCategoryRequest request);
    void delete(Long id);
}
