package com.food.manager.dto.request.fridge;

public record RemoveProductFromFridgeRequest(Long fridgeId, Long fridgeProductId, int quantity) {
}
