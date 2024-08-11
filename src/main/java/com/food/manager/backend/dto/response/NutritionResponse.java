package com.food.manager.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NutritionResponse {
    private Long nutritionId;
    private int calories;
    private float protein;
    private float fat;
    private float carbohydrate;

    @Override
    public String toString() {
        return "Kcal: " + calories + " | Protein: " + protein + " | Fat: " + fat + " | Carbs: " + carbohydrate;
    }
}
