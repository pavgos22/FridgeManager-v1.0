package com.food.manager.backend.exception;

public class NegativeValueException extends RuntimeException {
    public NegativeValueException() {
        super("Quantity must be greater than zero");
    }
}
