package com.food.manager.dto.response;

import com.food.manager.entity.ShoppingListItem;
import com.food.manager.entity.User;

import java.time.LocalDateTime;

public record CommentResponse(Long CommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, ShoppingListItem item, User author) {

}
