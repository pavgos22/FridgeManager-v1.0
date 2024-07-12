package com.food.manager.enums;

public enum QuantityType {
    GRAM("g"),
    MILLILITER("ml"),
    PIECE("pcs");

    private final String value;

    QuantityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
