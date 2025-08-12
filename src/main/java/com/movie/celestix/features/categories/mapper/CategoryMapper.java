package com.movie.celestix.features.categories.mapper;

import com.movie.celestix.common.models.Category;
import com.movie.celestix.features.categories.dto.CategoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toDto(Category category);
    List<CategoryResponse> toDtoList(List<Category> categories);
}
