package com.food.manager.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecipeNutrition {
    private int calories;
    private float protein;
    private float fat;
    private float carbohydrate;
}
