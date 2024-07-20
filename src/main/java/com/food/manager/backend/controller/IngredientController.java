package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.ingredient.CreateIngredientRequest;
import com.food.manager.backend.dto.response.IngredientResponse;
import com.food.manager.backend.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> getIngredient(@PathVariable Long id) {
        IngredientResponse ingredientResponse = ingredientService.getIngredient(id);
        return ResponseEntity.ok(ingredientResponse);
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(@RequestBody CreateIngredientRequest createIngredientRequest) {
        IngredientResponse ingredientResponse = ingredientService.createIngredient(createIngredientRequest);
        return ResponseEntity.ok(ingredientResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllIngredients() {
        ingredientService.deleteAllIngredients();
        return ResponseEntity.noContent().build();
    }
}
