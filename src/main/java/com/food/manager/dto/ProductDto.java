package com.food.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductDto {
    private Long productId;
    private String productName;
    private Long nutritionId;
    private Long shoppingListItemId;
    private List<Long> recipeIds;
    private List<Long> fridgeProductIds;
}
