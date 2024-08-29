package com.food.manager.backend.exception;

public class FridgeNotFoundException extends RuntimeException {
    public FridgeNotFoundException(long id) {
        super("Fridge with ID: " + id + " not found");
    }
}

