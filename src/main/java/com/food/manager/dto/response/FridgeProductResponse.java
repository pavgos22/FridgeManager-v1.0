package com.food.manager.dto.response;

import com.food.manager.entity.Fridge;
import com.food.manager.entity.Product;

public record FridgeProductResponse(Long fridgeProductId, String quantityType, int quantity, Fridge fridge, Product product) {
}
