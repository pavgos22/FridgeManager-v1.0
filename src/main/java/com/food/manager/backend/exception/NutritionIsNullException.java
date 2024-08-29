package com.food.manager.backend.exception;

public class NutritionIsNullException extends RuntimeException {
    public NutritionIsNullException(String productName) {
        super("Nutrition for product: " + productName + " is null");
    }
}
