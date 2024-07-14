package com.food.manager.exception;

public class FridgeProductNotFoundException extends RuntimeException {
    public FridgeProductNotFoundException(String message) {
        super(message);
    }
}
