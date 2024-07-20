package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.*;

import java.util.List;

public record ProductResponse(Long productId, String productName, Nutrition nutrition, List<Long> recipeIds) {
}
