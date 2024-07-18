package com.food.manager.dto.request.item;

import com.food.manager.enums.QuantityType;

public record AddItemToListRequest(Long productId, Long groupId, QuantityType quantityType, int quantity) {
}
