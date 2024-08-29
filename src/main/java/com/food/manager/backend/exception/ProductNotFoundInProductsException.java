package com.food.manager.backend.exception;

public class ProductNotFoundInProductsException extends RuntimeException {
    public ProductNotFoundInProductsException(long id) {
        super("Product with ID: " + id + " not found");
    }
}
