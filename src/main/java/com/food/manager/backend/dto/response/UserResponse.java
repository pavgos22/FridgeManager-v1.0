package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(long userId, String username, String firstName, String lastName, String email, LocalDateTime createdAt, LocalDateTime updatedAt, List<Long> groupIds, List<Comment> comments) {
}
