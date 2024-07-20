package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.IngredientResponse;
import com.food.manager.backend.entity.Ingredient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientMapper {

    public IngredientResponse toIngredientResponse(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getIngredientId(),
                ingredient.getQuantityType(),
                ingredient.getQuantity(),
                ingredient.isRequired(),
                ingredient.isIgnoreGroup(),
                ingredient.getProduct().getProductId(),
                ingredient.getRecipe() != null ? ingredient.getRecipe().getRecipeId() : null
        );
    }

    public List<IngredientResponse> mapToIngredientList(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(this::toIngredientResponse)
                .collect(Collectors.toList());
    }
}
