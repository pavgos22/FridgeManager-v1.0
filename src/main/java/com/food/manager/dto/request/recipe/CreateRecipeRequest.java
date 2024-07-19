package com.food.manager.dto.request.recipe;

import com.food.manager.enums.RecipeType;
import com.food.manager.enums.Weather;

import java.util.Set;

public record CreateRecipeRequest(String recipeName, String description, RecipeType recipeType, Weather weather, Set<Long> ingredientIds, String recipeURL) {
}
