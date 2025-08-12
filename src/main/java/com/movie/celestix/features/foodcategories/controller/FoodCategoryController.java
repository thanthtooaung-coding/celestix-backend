package com.movie.celestix.features.foodcategories.controller;

import com.movie.celestix.features.foodcategories.dto.FoodCategoryResponse;
import com.movie.celestix.features.foodcategories.dto.CreateFoodCategoryRequest;
import com.movie.celestix.features.foodcategories.dto.UpdateFoodCategoryRequest;
import com.movie.celestix.features.foodcategories.service.FoodCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @PostMapping
    public ResponseEntity<FoodCategoryResponse> create(@RequestBody final CreateFoodCategoryRequest request) {
        final FoodCategoryResponse createdCategory = this.foodCategoryService.create(request);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodCategoryResponse> retrieveOne(@PathVariable final Long id) {
        return ResponseEntity.ok(this.foodCategoryService.retrieveOne(id));
    }

    @GetMapping
    public ResponseEntity<List<FoodCategoryResponse>> retrieveAll() {
        return ResponseEntity.ok(this.foodCategoryService.retrieveAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodCategoryResponse> update(@PathVariable final Long id, @RequestBody final UpdateFoodCategoryRequest request) {
        return ResponseEntity.ok(this.foodCategoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        this.foodCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
