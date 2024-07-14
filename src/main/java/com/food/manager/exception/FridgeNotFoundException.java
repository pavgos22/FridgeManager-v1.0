package com.food.manager.exception;

public class FridgeNotFoundException extends RuntimeException {
    public FridgeNotFoundException(String message) {
        super(message);
    }
}

