package com.food.manager.backend.exception;

public class NotUsersCommentException extends RuntimeException {
    public NotUsersCommentException(long userId, long commentId) {
        super("User with ID: " + userId + " is not an author of a comment with ID: " + commentId);
    }
}
