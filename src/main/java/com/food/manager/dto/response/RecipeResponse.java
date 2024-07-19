package com.food.manager.dto.response;

import com.food.manager.entity.Ingredient;
import java.util.Set;

public record RecipeResponse(Long recipeId, String recipeName, String description, int numberOfServings, String recipeType, String weather, Set<Ingredient> ingredients, String recipeUrl) {
}
