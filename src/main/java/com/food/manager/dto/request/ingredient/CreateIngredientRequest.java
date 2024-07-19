package com.food.manager.dto.request.ingredient;

import com.food.manager.enums.QuantityType;

public record CreateIngredientRequest(QuantityType quantityType, int quantity, boolean required, boolean ignoreGroup, Long productId) {
}
