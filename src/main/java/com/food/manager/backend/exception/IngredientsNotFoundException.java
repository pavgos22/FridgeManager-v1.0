package com.food.manager.backend.exception;

public class IngredientsNotFoundException extends RuntimeException {
    public IngredientsNotFoundException() {
        super("One or more ingredients not found");
    }
}

