package com.food.manager.dto.response;

import com.food.manager.entity.Product;

import java.util.List;

public record RecipeResponse(Long recipeId, String description, int numberOfServings, String recipeType, String weather, List<Product> products) {
}
