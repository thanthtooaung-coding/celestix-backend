package com.movie.celestix.features.food.controller;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.food.dto.CreateFoodOrderRequest;
import com.movie.celestix.features.food.dto.FoodOrderHistoryResponse;
import com.movie.celestix.features.food.dto.FoodOrderResponse;
import com.movie.celestix.features.food.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food-orders")
@RequiredArgsConstructor
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    @PostMapping
    public ResponseEntity<ApiResponse<FoodOrderResponse>> createFoodOrder(
            @RequestBody CreateFoodOrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        FoodOrderResponse order = foodOrderService.createFoodOrder(request, userDetails.getUsername());
        return ApiResponse.created(order, "Food order created successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<FoodOrderHistoryResponse>>> getMyFoodOrders(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<FoodOrderHistoryResponse> history = foodOrderService.getFoodOrderHistory(userDetails.getUsername());
        return ApiResponse.ok(history, "Food order history retrieved successfully");
    }
}
