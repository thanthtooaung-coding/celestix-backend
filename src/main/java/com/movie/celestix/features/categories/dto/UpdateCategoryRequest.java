package com.movie.celestix.features.categories.dto;

public record UpdateCategoryRequest(
        String name,
        String description
) {}