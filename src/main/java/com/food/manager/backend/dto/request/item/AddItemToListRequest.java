package com.food.manager.backend.dto.request.item;

import com.food.manager.backend.enums.QuantityType;

public record AddItemToListRequest(Long productId, Long groupId, QuantityType quantityType, int quantity) {
}
