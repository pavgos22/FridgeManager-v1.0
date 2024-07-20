package com.food.manager.backend.dto.request.group;

public record AddItemToGroupRequest(Long groupId, Long shoppingItemId) {
}
