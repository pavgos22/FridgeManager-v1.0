package com.food.manager.backend.exception;

public class ShoppingListItemNotInGroupException extends RuntimeException {
    public ShoppingListItemNotInGroupException(long itemId, long groupId) {
        super("Item with ID: " + itemId + " does not belong to group with ID: " + groupId);
    }
}
