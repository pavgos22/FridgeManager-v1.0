package com.food.manager.backend.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(long id) {
        super("Comment with ID: " + id + " not found");
    }
}
