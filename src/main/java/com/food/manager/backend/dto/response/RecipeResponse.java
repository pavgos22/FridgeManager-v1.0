package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.Ingredient;
import java.util.Set;

public record RecipeResponse(Long recipeId, String recipeName, String description, String recipeType, String weather, Set<Ingredient> ingredients, String recipeUrl) {
}
