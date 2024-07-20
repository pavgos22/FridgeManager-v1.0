package com.food.manager.backend.dto.response;

import com.food.manager.backend.enums.QuantityType;


public record ShoppingListItemResponse(Long itemId, Long productId, Long groupId, QuantityType quantityType, int quantity, boolean checked) {
}
