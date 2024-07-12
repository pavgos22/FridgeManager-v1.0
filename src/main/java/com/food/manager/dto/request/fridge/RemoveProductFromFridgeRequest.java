package com.food.manager.dto.request.fridge;

import com.food.manager.enums.QuantityType;

public record RemoveProductFromFridgeRequest(Long fridgeId, Long fridgeProductId, QuantityType quantityType, int quantity) {
}
