package com.food.manager.backend.dto.response;

import com.food.manager.backend.enums.QuantityType;

public record FridgeProductResponse(Long fridgeProductId, QuantityType quantityType, int quantity, Long fridgeId, String productName) {
}
