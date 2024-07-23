package com.food.manager.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String productName;
    private NutritionResponse nutrition;
    private List<Long> recipeIds;
}
