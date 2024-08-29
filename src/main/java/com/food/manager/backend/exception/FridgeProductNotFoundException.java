package com.food.manager.backend.exception;

public class FridgeProductNotFoundException extends RuntimeException {
    public FridgeProductNotFoundException(long id) {
        super("Fridge product with ID: " + id + " not found");
    }
}
