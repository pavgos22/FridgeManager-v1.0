package com.food.manager.backend.exception;

public class ShoppingListItemNotFoundException extends RuntimeException {
    public ShoppingListItemNotFoundException(String message) {
        super(message);
    }
}
