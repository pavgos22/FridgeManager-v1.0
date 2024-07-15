package com.food.manager.dto.response;

import com.food.manager.enums.QuantityType;

public record FridgeProductResponse(Long fridgeProductId, QuantityType quantityType, int quantity, Long fridgeId, String productName) {
}
