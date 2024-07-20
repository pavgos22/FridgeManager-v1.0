package com.food.manager.backend.exception;

public class FridgeNotFoundException extends RuntimeException {
    public FridgeNotFoundException(String message) {
        super(message);
    }
}

