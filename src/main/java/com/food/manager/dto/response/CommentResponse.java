package com.food.manager.dto.response;

import com.food.manager.entity.ShoppingListItem;

import java.time.LocalDateTime;

public record CommentResponse(Long CommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, ShoppingListItem item, Long authorId) {

}
