package com.food.manager.backend.exception;

public class UserAlreadyExistsInGroupException extends RuntimeException {
    public UserAlreadyExistsInGroupException(long userId, long groupId) {
        super("User with ID: " + userId + " already exists in group with ID: " + groupId);
    }
}