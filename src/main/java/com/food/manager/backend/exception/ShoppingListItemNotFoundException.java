package com.food.manager.backend.exception;

public class ShoppingListItemNotFoundException extends RuntimeException {
    public ShoppingListItemNotFoundException(long id) {
        super("Item with ID: " + id + " not found");
    }
}
