package com.food.manager.backend.exception;

public class DuplicateIngredientException extends RuntimeException {
    public DuplicateIngredientException() {
        super("Duplicate ingredients found");
    }
}
