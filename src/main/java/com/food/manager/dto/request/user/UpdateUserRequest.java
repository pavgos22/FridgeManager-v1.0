package com.food.manager.dto.request.user;

public record UpdateUserRequest(String username, String firstName, String lastName, String email, String password) {
}
