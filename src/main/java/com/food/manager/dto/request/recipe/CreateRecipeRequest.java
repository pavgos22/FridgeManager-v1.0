package com.food.manager.dto.request.recipe;

import com.food.manager.entity.Ingredient;
import com.food.manager.entity.Product;
import com.food.manager.enums.RecipeType;
import com.food.manager.enums.Weather;

import java.util.List;

public record CreateRecipeRequest(String description, int numberOfServings, RecipeType recipeType, Weather weather, List<Ingredient> ingredients, String recipeURL) {
}
