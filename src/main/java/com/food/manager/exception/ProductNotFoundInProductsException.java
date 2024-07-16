package com.food.manager.exception;

public class ProductNotFoundInProductsException extends RuntimeException {
    public ProductNotFoundInProductsException(String message) {
        super(message);
    }
}
