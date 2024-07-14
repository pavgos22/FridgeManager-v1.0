package com.food.manager.dto.request.fridge;

import com.food.manager.enums.QuantityType;

public record AddProductRequest(Long fridgeId, String productName, QuantityType quantityType, int quantity) {
}
