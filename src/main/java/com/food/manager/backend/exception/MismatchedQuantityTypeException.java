package com.food.manager.backend.exception;

public class MismatchedQuantityTypeException extends RuntimeException {
    public MismatchedQuantityTypeException(String message) {
        super(message);
    }
}
