package com.food.manager.backend.exception;

public class FridgeProductNotFoundException extends RuntimeException {
    public FridgeProductNotFoundException(String message) {
        super(message);
    }
}
