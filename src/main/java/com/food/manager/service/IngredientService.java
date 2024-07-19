package com.food.manager.service;

import com.food.manager.dto.request.ingredient.CreateIngredientRequest;
import com.food.manager.dto.response.IngredientResponse;
import com.food.manager.entity.Ingredient;
import com.food.manager.entity.Product;
import com.food.manager.exception.IngredientNotFoundException;
import com.food.manager.exception.ProductNotFoundInProductsException;
import com.food.manager.mapper.IngredientMapper;
import com.food.manager.repository.IngredientRepository;
import com.food.manager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private ProductRepository productRepository;

    public IngredientResponse getIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found with id: " + id));
        return ingredientMapper.toIngredientResponse(ingredient);
    }

    public List<IngredientResponse> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(ingredientMapper::toIngredientResponse)
                .collect(Collectors.toList());
    }

    public IngredientResponse createIngredient(CreateIngredientRequest createIngredientRequest) {
        Product product = productRepository.findById(createIngredientRequest.productId()).orElseThrow(() -> new ProductNotFoundInProductsException("Product not found with id: " + createIngredientRequest.productId()));
        Ingredient ingredient = new Ingredient(createIngredientRequest.quantityType(), createIngredientRequest.quantity(), createIngredientRequest.required(), createIngredientRequest.required(), product);
        return ingredientMapper.toIngredientResponse(ingredientRepository.save(ingredient));
    }
}
