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
public class ShoppingListItemResponse {
    private Long itemId;
    private Long productId;
    private Long groupId;
    private QuantityType quantityType;
    private int quantity;
    private boolean checked;
}
