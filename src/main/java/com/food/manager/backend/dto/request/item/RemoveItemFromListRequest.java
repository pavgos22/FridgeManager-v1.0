package com.food.manager.backend.dto.request.item;

public record RemoveItemFromListRequest(Long itemId, Long groupId, int quantity) {
}
