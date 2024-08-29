package com.food.manager.backend.exception;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(long id) {
        super("Ingredient with ID: " + id + " not found");
    }
}
