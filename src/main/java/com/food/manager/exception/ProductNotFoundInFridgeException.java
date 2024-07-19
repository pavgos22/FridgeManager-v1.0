package com.food.manager.exception;

public class ProductNotFoundInFridgeException extends RuntimeException {
    public ProductNotFoundInFridgeException(String message) {
        super(message);
    }
}
