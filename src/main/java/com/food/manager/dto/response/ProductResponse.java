package com.food.manager.dto.response;

import com.food.manager.entity.*;

import java.util.List;

public record ProductResponse(Long productId, String productName, Nutrition nutrition, List<Recipe> recipes) {
}
