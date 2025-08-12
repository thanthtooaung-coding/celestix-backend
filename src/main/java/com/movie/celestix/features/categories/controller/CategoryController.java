package com.movie.celestix.features.categories.controller;

import com.movie.celestix.features.categories.dto.CategoryResponse;
import com.movie.celestix.features.categories.dto.CreateCategoryRequest;
import com.movie.celestix.features.categories.dto.UpdateCategoryRequest;
import com.movie.celestix.features.categories.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody final CreateCategoryRequest request) {
        final CategoryResponse createdCategory = this.categoryService.create(request);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> retrieveOne(@PathVariable final Long id) {
        return ResponseEntity.ok(this.categoryService.retrieveOne(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> retrieveAll() {
        return ResponseEntity.ok(this.categoryService.retrieveAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable final Long id, @RequestBody final UpdateCategoryRequest request) {
        return ResponseEntity.ok(this.categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        this.categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
