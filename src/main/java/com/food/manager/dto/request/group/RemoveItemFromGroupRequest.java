package com.food.manager.dto.request.group;

public record RemoveItemFromGroupRequest(Long groupId, Long itemId, int quantity) {
}
