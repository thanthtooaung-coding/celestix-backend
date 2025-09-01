package com.movie.celestix.features.food.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.movie.celestix.common.repository.jpa.ComboJpaRepository;
import com.movie.celestix.common.repository.jpa.FoodJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.movie.celestix.common.enums.Category;
import com.movie.celestix.common.models.Combo;
import com.movie.celestix.common.models.Food;

@Service
@AllArgsConstructor
public class FoodService {
    private final FoodJpaRepository foodRepo;
    private final ComboJpaRepository comboRepo;

    public List<Food> all() {
        List<Food> foods = foodRepo.findAll();
        return foods;
    }

    public Optional<Food> getFoodById(Long id) {
        return foodRepo.findById(id);
    }

    public void add(Food ff) {
        Food food = new Food();
        food.setName(ff.getName());
        food.setPrice(ff.getPrice());
        food.setCategory(ff.getCategory());
        food.setAllergens(ff.getAllergens());
        food.setDescription(ff.getDescription());
        food.setPhotoUrl(ff.getPhotoUrl());
        foodRepo.save(food);
    }

    public void modify(Long id, Food ff) {
        Food food = foodRepo.findById(id).orElse(null);
        if (food != null) {
            food.setName(ff.getName());
            food.setPrice(ff.getPrice());
            food.setCategory(ff.getCategory());
            food.setAllergens(ff.getAllergens());
            food.setDescription(ff.getDescription());
            food.setPhotoUrl(ff.getPhotoUrl());
            foodRepo.save(food);
        }
    }

    public void drop(Long id) {
        foodRepo.deleteById(id);
    }

    public boolean isIncludedInCombo(Long foodId) {
        return comboRepo.existsByFoods_Id(foodId); // assuming you have JPA relationship
    }

    public List<Food> filter(String category) {
        if (category.equalsIgnoreCase("none")) {
            return foodRepo.findAll();
        }

        try {
            Category catEnum = Category.valueOf(category.toUpperCase());
            return foodRepo.findByCategory(catEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category! Only snacks, drinks, food, or none are allowed.");
        }
    }

    public List<Combo> getAllCombos() {
        return comboRepo.findAll();
    }

    public Optional<Combo> getComboById(Long id) {
        return comboRepo.findById(id);
    }

    public Combo createCombo(String comboName, List<Long> foodIds, String photoUrl) {
        // ✅ Validation rules
        if (foodIds.size() < 2) {
            throw new IllegalArgumentException("A combo must have at least 2 food items.");
        }
        if (foodIds.size() > 5) {
            throw new IllegalArgumentException("A combo cannot exceed 5 food items.");
        }

        // Fetch selected foods (preserve duplicates)
        List<Food> selectedFoods = foodIds.stream()
                .map(id -> foodRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Food not found: " + id)))
                .toList();

        // Calculate total price
        double sumPrice = selectedFoods.stream()
                .mapToDouble(Food::getPrice)
                .sum();

        // Apply discount
        double discountRate = switch (foodIds.size()) {
            case 2 -> 0.03;
            case 3 -> 0.05;
            case 4 -> 0.08;
            case 5 -> 0.10;
            default -> 0.0;
        };

        double finalPrice = sumPrice * (1 - discountRate);

        // Create combo
        Combo combo = new Combo();
        combo.setComboName(comboName);
        combo.setFoods(selectedFoods); // ✅ keeps duplicates
        combo.setComboPrice(finalPrice);
        combo.setPhotoUrl(photoUrl);
        return comboRepo.save(combo);
    }

    public Combo updateCombo(Long comboId, String comboName, List<Long> foodIds) {
        Combo combo = comboRepo.findById(comboId)
                .orElseThrow(() -> new RuntimeException("Combo not found"));

        if (comboName != null) {
            combo.setComboName(comboName);
        }

        if (foodIds != null && !foodIds.isEmpty()) {
            // ✅ Preserve duplicates by fetching manually
            List<Food> selectedFoods = new ArrayList<>();
            for (Long id : foodIds) {
                foodRepo.findById(id).ifPresent(selectedFoods::add);
                // if not found → skip (no crash)
            }

            combo.setFoods(selectedFoods);

            // ✅ Sum price including duplicates
            double sumPrice = selectedFoods.stream()
                    .mapToDouble(Food::getPrice)
                    .sum();

            // ✅ Apply discount including duplicates
            double discountRate = switch (selectedFoods.size()) {
                case 2 -> 0.03;
                case 3 -> 0.05;
                case 4 -> 0.08;
                case 5 -> 0.10;
                default -> 0.0;
            };

            double finalPrice = sumPrice * (1 - discountRate);
            combo.setComboPrice(finalPrice);
        }

        return comboRepo.save(combo);
    }

    @Transactional
    public void deleteCombo(Long id) {
        comboRepo.deleteById(id);
    }
}
