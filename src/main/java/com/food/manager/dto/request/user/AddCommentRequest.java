package com.food.manager.dto.request.user;

public record AddCommentRequest(Long userId, Long itemId, String content) {
}
