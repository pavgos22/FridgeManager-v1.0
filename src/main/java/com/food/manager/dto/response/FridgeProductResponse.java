package com.food.manager.dto.response;

import com.food.manager.entity.Fridge;
import com.food.manager.entity.Product;
import com.food.manager.enums.QuantityType;

public record FridgeProductResponse(Long fridgeProductId, QuantityType quantityType, int quantity, Long fridgeId, Product product) {
}
