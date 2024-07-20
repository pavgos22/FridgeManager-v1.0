package com.food.manager.backend.dto.request.recipe;

import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;

import java.util.Set;

public record CreateRecipeRequest(String recipeName, String description, RecipeType recipeType, Weather weather, Set<Long> ingredientIds, String recipeURL) {
}
