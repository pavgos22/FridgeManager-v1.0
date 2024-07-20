package com.food.manager.backend.dto.request.user;

public record AddCommentRequest(Long userId, Long itemId, String content) {
}
