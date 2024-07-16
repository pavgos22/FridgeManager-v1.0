package com.food.manager.dto.request.item;

public record RemoveItemFromListRequest(Long itemId, Long groupId, int quantity) {
}
