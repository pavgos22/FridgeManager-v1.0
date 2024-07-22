package com.food.manager.backend.dto.response;

import com.food.manager.backend.enums.QuantityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IngredientResponse {
    private Long ingredientId;
    private QuantityType quantityType;
    private int quantity;
    private boolean required;
    private boolean ignoreGroup;
    private Long productId;
    private Long recipeId;
}
