package com.food.manager.dto.response;

import com.food.manager.entity.Product;

public record NutritionResponse(Long nutritionId, int calories, float protein, float fat, float carbohydrate) {
}
