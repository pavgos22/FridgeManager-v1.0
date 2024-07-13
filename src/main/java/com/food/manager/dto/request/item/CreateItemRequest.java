package com.food.manager.dto.request.item;

import com.food.manager.enums.QuantityType;

public record CreateItemRequest(Long productId, QuantityType quantityType, int quantity, Long groupId) {
}
