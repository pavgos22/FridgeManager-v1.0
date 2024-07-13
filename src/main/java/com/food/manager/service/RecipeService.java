package com.food.manager.service;

import com.food.manager.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.dto.response.RecipeResponse;
import com.food.manager.entity.Recipe;
import com.food.manager.mapper.RecipeMapper;
import com.food.manager.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        Recipe recipe = recipeMapper.toEntity(createRecipeRequest);
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toRecipeResponse(recipe);
    }

    public RecipeResponse getRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            return recipeMapper.toRecipeResponse(recipeOptional.get());
        } else {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
    }

    public List<RecipeResponse> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipeMapper.mapToRecipeResponseList(recipes);
    }
}
