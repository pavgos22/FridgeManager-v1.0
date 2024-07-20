package com.food.manager.backend.dto.request.fridge;

public record RemoveProductFromFridgeRequest(Long fridgeId, Long fridgeProductId, int quantity) {
}
