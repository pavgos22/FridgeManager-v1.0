package com.food.manager.controller;

import com.food.manager.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.dto.response.RecipeResponse;
import com.food.manager.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody CreateRecipeRequest createRecipeRequest) {
        RecipeResponse recipeResponse = recipeService.createRecipe(createRecipeRequest);
        return ResponseEntity.ok(recipeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable Long id) {
        RecipeResponse recipeResponse = recipeService.getRecipe(id);
        return ResponseEntity.ok(recipeResponse);
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAllRecipes() {
        List<RecipeResponse> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }
}
