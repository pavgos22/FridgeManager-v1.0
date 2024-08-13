package com.food.manager.backend.dto.response;

import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private Long recipeId;
    private String recipeName;
    private String description;
    private RecipeType recipeType;
    private Weather weather;
    private Set<IngredientResponse> ingredients;
    private String recipeUrl;
}
