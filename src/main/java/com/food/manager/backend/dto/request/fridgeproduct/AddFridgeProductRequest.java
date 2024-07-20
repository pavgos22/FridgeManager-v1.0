package com.food.manager.backend.dto.request.fridgeproduct;

import com.food.manager.backend.enums.QuantityType;

public record AddFridgeProductRequest(QuantityType quantityType, int quantity, Long productId) {
}