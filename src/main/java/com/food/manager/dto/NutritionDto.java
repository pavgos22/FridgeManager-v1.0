package com.food.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NutritionDto {
    private Long nutritionId;
    private int calories;
    private float protein;
    private float fat;
    private float carbohydrate;
    private Long productId;
}
