package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.fridge.AddProductRequest;
import com.food.manager.backend.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.backend.dto.response.FridgeProductResponse;
import com.food.manager.backend.dto.response.FridgeResponse;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.service.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fridges")
public class FridgeController {

    @Autowired
    private FridgeService fridgeService;

    @PutMapping("/addProduct")
    public ResponseEntity<FridgeResponse> addProductToFridge(@RequestBody AddProductRequest addProductRequest) {
        FridgeResponse fridgeResponse = fridgeService.addProductToFridge(addProductRequest);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PutMapping("/removeProduct")
    public ResponseEntity<FridgeResponse> removeProduct(@RequestBody RemoveProductFromFridgeRequest removeProductRequest) {
        FridgeResponse fridgeResponse = fridgeService.removeProductFromFridge(removeProductRequest);
        return ResponseEntity.ok(fridgeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FridgeResponse> getFridge(@PathVariable Long id) {
        FridgeResponse fridgeResponse = fridgeService.getFridge(id);
        return ResponseEntity.ok(fridgeResponse);
    }

    @GetMapping
    public ResponseEntity<List<FridgeResponse>> getAllFridges() {
        List<FridgeResponse> fridges = fridgeService.getAllFridges();
        return ResponseEntity.ok(fridges);
    }

    @GetMapping("/recipes/{fridgeId}")
    public ResponseEntity<List<RecipeResponse>> getRecipesPossibleWithFridgeProducts(@PathVariable Long fridgeId) {
        List<RecipeResponse> recipes = fridgeService.getRecipesPossibleWithFridgeProducts(fridgeId);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{fridgeId}/products")
    public ResponseEntity<List<FridgeProductResponse>> getFridgeProducts(@PathVariable Long fridgeId) {
        List<FridgeProductResponse> fridgeProducts = fridgeService.getFridgeProducts(fridgeId);
        return ResponseEntity.ok(fridgeProducts);
    }

    @PutMapping("/{fridgeId}/executeRecipe/{recipeId}")
    public ResponseEntity<FridgeResponse> executeRecipe(@PathVariable Long fridgeId, @PathVariable Long recipeId) {
        FridgeResponse fridgeResponse = fridgeService.executeRecipe(fridgeId, recipeId);
        return ResponseEntity.ok(fridgeResponse);
    }
}
