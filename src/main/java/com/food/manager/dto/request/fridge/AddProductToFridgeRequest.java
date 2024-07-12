package com.food.manager.dto.request.fridge;

import com.food.manager.enums.QuantityType;

public record AddProductToFridgeRequest(Long fridgeId, Long FridgeProductId, QuantityType quantityType, int quantity) {
}
