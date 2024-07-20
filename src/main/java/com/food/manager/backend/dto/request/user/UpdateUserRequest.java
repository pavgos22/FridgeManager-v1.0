package com.food.manager.backend.dto.request.user;

public record UpdateUserRequest(String username, String firstName, String lastName, String email, String password) {
}
