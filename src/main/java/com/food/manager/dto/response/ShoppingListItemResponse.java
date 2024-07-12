package com.food.manager.dto.response;

import com.food.manager.entity.Comment;
import com.food.manager.entity.Group;

import java.util.List;

public record ShoppingListItemResponse(Long itemId, String quantityType, int quantity, boolean checked, List<Comment> comments, Group group) {
}
