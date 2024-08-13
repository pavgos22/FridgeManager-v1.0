package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.entity.Recipe;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeMapper {

    private final IngredientMapper ingredientMapper;

    public RecipeMapper(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public RecipeResponse toRecipeResponse(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        return new RecipeResponse(
                recipe.getRecipeId(),
                recipe.getRecipeName(),
                recipe.getDescription(),
                recipe.getRecipeType(),
                recipe.getWeather() != null ? recipe.getWeather() : null,
                ingredientMapper.mapToIngredientSet(recipe.getIngredients()),
                recipe.getRecipeUrl()
        );
    }

    public List<RecipeResponse> mapToRecipeResponseList(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::toRecipeResponse)
                .collect(Collectors.toList());
    }
}
