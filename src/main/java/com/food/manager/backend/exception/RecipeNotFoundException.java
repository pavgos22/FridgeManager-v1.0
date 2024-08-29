package com.food.manager.backend.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(long id) {
        super("Recipe with ID: " + id + " not found");
    }
}
