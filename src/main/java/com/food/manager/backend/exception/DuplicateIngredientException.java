package com.food.manager.backend.exception;

public class DuplicateIngredientException extends RuntimeException {
    public DuplicateIngredientException(String message) {
        super(message);
    }
}
