package com.food.manager.backend.enums;

import lombok.Getter;

@Getter
public enum QuantityType {
    GRAM("g"),
    MILLILITER("ml"),
    LITER("l"),
    PIECE("pcs");

    private final String value;

    QuantityType(String value) {
        this.value = value;
    }

}
