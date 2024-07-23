package com.food.manager.backend.dto.response;

import com.food.manager.backend.enums.QuantityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FridgeProductResponse {
    private Long fridgeProductId;
    private QuantityType quantityType;
    private int quantity;
    private String productName;
}
