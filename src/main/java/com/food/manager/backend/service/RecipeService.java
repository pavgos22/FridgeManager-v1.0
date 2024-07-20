package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.entity.Ingredient;
import com.food.manager.backend.entity.Recipe;
import com.food.manager.backend.exception.DuplicateIngredientException;
import com.food.manager.backend.exception.IngredientNotFoundException;
import com.food.manager.backend.mapper.RecipeMapper;
import com.food.manager.backend.repository.IngredientRepository;
import com.food.manager.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeMapper recipeMapper;

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

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        Recipe recipe = new Recipe(createRecipeRequest.recipeName(), createRecipeRequest.description(), createRecipeRequest.recipeType(), createRecipeRequest.weather(), createRecipeRequest.recipeURL());

        recipeRepository.save(recipe);

        List<Ingredient> ingredients = ingredientRepository.findAllById(createRecipeRequest.ingredientIds());
        if (ingredients.size() != createRecipeRequest.ingredientIds().size())
            throw new IngredientNotFoundException("One or more ingredients not found");

        Set<Ingredient> uniqueIngredients = new HashSet<>(ingredients);
        if (uniqueIngredients.size() != ingredients.size())
            throw new DuplicateIngredientException("Duplicate ingredients found");

        recipe.setIngredients(uniqueIngredients);

        for (Ingredient ingredient : uniqueIngredients) {
            ingredient.setRecipe(recipe);
            ingredientRepository.save(ingredient);
        }

        recipeRepository.save(recipe);

        return recipeMapper.toRecipeResponse(recipe);
    }

    public void deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }

}
