package com.food.manager.mapper;

import com.food.manager.dto.response.IngredientResponse;
import com.food.manager.entity.Ingredient;
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
                ingredient.getRecipe().getRecipeId()
        );
    }

    public List<IngredientResponse> mapToIngredientList(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(this::toIngredientResponse)
                .collect(Collectors.toList());
    }
}
