package com.food.manager.backend.exception;

public class UserNotInGroupException extends RuntimeException {
    public UserNotInGroupException(long userId, long groupId) {
        super("User with ID: " + userId + " does not exist in group with ID: " + groupId);
    }
}