package com.food.manager.backend.dto.request.user;

public record EditCommentRequest(Long AuthorId, String content) {
}
