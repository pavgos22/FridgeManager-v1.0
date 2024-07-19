package com.food.manager.dto.response;

import com.food.manager.enums.QuantityType;

public record IngredientResponse(Long ingredientId, QuantityType quantityType, int quantity, boolean required, boolean ignoreGroup, Long productId, Long recipeId) {

}
