package com.movie.celestix.features.categories.service.impl;

import com.movie.celestix.common.models.Category;
import com.movie.celestix.common.repository.jpa.CategoryJpaRepository;
import com.movie.celestix.features.categories.dto.CategoryResponse;
import com.movie.celestix.features.categories.dto.CreateCategoryRequest;
import com.movie.celestix.features.categories.dto.UpdateCategoryRequest;
import com.movie.celestix.features.categories.mapper.CategoryMapper;
import com.movie.celestix.features.categories.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        final Category category = new Category(request.name(), request.description());
        final Category savedCategory = this.categoryJpaRepository.save(category);
        return this.categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse retrieveOne(Long id) {
        final Category category = this.categoryJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id " + id + " not found"));
        return this.categoryMapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> retrieveAll() {
        final List<Category> categories = categoryJpaRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        final Category category = this.categoryJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id " + id + " not found"));
        category.setName(request.name());
        category.setDescription(request.description());
        final Category updatedCategory = this.categoryJpaRepository.save(category);
        return this.categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryJpaRepository.existsById(id)) {
            throw new RuntimeException("Category with id " + id + " not found");
        }
        this.categoryJpaRepository.deleteById(id);
    }
}
