package com.food.manager.backend.dto.response;

import com.food.manager.backend.enums.QuantityType;

public record IngredientResponse(Long ingredientId, QuantityType quantityType, int quantity, boolean required, boolean ignoreGroup, Long productId, Long recipeId) {

}
