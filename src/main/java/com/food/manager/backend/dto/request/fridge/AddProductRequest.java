package com.food.manager.backend.dto.request.fridge;

import com.food.manager.backend.enums.QuantityType;

public record AddProductRequest(Long fridgeId, Long productId, QuantityType quantityType, int quantity) {
}