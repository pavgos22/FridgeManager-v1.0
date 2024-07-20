package com.food.manager.backend.exception;

public class ProductNotFoundInProductsException extends RuntimeException {
    public ProductNotFoundInProductsException(String message) {
        super(message);
    }
}
