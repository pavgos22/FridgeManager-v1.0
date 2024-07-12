package com.food.manager.dto.response;

import com.food.manager.entity.Comment;
import com.food.manager.entity.Group;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(long userId, String username, String firstName, String lastName, String email, String password, LocalDateTime createdAt, LocalDateTime updatedAt, List<Group> groups, List<Comment> comments) {
}
