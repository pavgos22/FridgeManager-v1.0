package com.food.manager.backend.dto.request.item;

import com.food.manager.backend.enums.QuantityType;

public record CreateItemRequest(Long productId, QuantityType quantityType, int quantity) {
}
