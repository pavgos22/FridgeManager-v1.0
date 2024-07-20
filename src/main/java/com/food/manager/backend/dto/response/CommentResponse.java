package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.ShoppingListItem;

import java.time.LocalDateTime;

public record CommentResponse(Long CommentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, ShoppingListItem item, Long authorId) {

}
