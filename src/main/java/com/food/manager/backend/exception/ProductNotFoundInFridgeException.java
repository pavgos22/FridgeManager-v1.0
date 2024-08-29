package com.food.manager.backend.exception;

public class ProductNotFoundInFridgeException extends RuntimeException {
    public ProductNotFoundInFridgeException(String productName, long fridgeId) {
        super("Product: " + productName + " not found in fridge with ID: " + fridgeId);
    }
}
