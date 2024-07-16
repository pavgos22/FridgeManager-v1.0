package com.food.manager.dto.request.fridgeproduct;

import com.food.manager.enums.QuantityType;

public record AddFridgeProductRequest(QuantityType quantityType, int quantity, String productName) {
}