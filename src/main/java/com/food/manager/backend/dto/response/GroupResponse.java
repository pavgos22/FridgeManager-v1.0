package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.Fridge;
import com.food.manager.backend.entity.ShoppingListItem;
import com.food.manager.backend.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public record GroupResponse(Long groupId, String groupName, LocalDateTime createdAt, LocalDateTime updatedAt, List<User> users, Fridge groupFridge, List<ShoppingListItem> shoppingListItems) {
}
