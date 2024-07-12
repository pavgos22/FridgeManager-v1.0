package com.food.manager.dto.request.fridgeproduct;

import com.food.manager.enums.QuantityType;

public record AddFridgeProductRequest(Long fridgeProductId, Long ProductId, QuantityType quantityType, int quantity) {

}
