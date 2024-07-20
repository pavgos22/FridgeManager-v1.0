package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.entity.Recipe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeMapper {

    public RecipeResponse toRecipeResponse(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        return new RecipeResponse(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getDescription(),
                recipe.getRecipeType().name(),
                recipe.getWeather() != null ? recipe.getWeather().name() : null,
                recipe.getIngredients(),
                recipe.getRecipeUrl()
        );
    }

    public List<RecipeResponse> mapToRecipeResponseList(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::toRecipeResponse)
                .collect(Collectors.toList());
    }
}
