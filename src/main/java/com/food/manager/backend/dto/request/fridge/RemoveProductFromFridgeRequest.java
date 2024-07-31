package com.food.manager.backend.dto.request.fridge;

public record RemoveProductFromFridgeRequest(Long fridgeProductId, int quantity) {
}
