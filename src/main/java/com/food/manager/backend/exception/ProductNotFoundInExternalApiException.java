package com.food.manager.backend.exception;

public class ProductNotFoundInExternalApiException extends RuntimeException {
    public ProductNotFoundInExternalApiException(String productName) {
        super("Product: " + productName + " not found in external API");
    }
}
