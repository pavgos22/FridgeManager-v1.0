package com.food.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FridgeProductDto {
    private Long fridgeProductId;
    private String quantityType;
    private int quantity;
    private Long fridgeId;
    private Long productId;
}
