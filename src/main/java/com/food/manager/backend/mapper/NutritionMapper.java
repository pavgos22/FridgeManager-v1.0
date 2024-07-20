package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.entity.Nutrition;
import org.springframework.stereotype.Service;

@Service
public class NutritionMapper {

    public NutritionResponse toNutritionResponse(Nutrition nutrition) {
        if (nutrition == null) {
            return null;
        }
        return new NutritionResponse(
                nutrition.getNutritionId(),
                nutrition.getCalories(),
                nutrition.getProtein(),
                nutrition.getFat(),
                nutrition.getCarbohydrate()
        );
    }

    public Nutrition toEntity(NutritionResponse nutritionResponse) {
        if (nutritionResponse == null) {
            return null;
        }
        Nutrition nutrition = new Nutrition();
        nutrition.setNutritionId(nutritionResponse.nutritionId());
        nutrition.setCalories(nutritionResponse.calories());
        nutrition.setProtein(nutritionResponse.protein());
        nutrition.setFat(nutritionResponse.fat());
        nutrition.setCarbohydrate(nutritionResponse.carbohydrate());
        return nutrition;
    }
}
