package com.food.manager.exception;

public class MismatchedQuantityTypeException extends RuntimeException {
    public MismatchedQuantityTypeException(String message) {
        super(message);
    }
}
