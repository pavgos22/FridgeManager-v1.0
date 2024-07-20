package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.Product;

public record NutritionResponse(Long nutritionId, int calories, float protein, float fat, float carbohydrate) {
}
