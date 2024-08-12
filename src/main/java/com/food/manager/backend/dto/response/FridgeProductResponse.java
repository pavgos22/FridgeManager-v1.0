package com.food.manager.backend.dto.response;

import com.food.manager.backend.enums.QuantityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FridgeProductResponse {
    private Long fridgeProductId;
    private QuantityType quantityType;
    private int quantity;
    private String productName;

    @Override
    public String toString() {
        return productName + " (" + quantity + " " + quantityType + ")";
    }
}
