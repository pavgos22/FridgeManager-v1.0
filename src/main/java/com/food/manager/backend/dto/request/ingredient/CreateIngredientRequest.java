package com.food.manager.backend.dto.request.ingredient;

import com.food.manager.backend.enums.QuantityType;

public record CreateIngredientRequest(QuantityType quantityType, int quantity, boolean required, boolean ignoreGroup, Long productId) {
}
