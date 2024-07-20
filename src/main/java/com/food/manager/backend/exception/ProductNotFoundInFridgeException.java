package com.food.manager.backend.exception;

public class ProductNotFoundInFridgeException extends RuntimeException {
    public ProductNotFoundInFridgeException(String message) {
        super(message);
    }
}
