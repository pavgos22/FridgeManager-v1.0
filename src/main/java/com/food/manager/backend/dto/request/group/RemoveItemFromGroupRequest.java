package com.food.manager.backend.dto.request.group;

public record RemoveItemFromGroupRequest(Long groupId, Long itemId, int quantity) {
}
