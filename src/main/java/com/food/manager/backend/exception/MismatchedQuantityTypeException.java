package com.food.manager.backend.exception;

public class MismatchedQuantityTypeException extends RuntimeException {
    public MismatchedQuantityTypeException(String productName) {
        super("Quantity type mismatch for product: " + productName);
    }
}
