package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.ingredient.CreateIngredientRequest;
import com.food.manager.backend.dto.response.IngredientResponse;
import com.food.manager.backend.entity.Ingredient;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.exception.IngredientNotFoundException;
import com.food.manager.backend.exception.ProductNotFoundInProductsException;
import com.food.manager.backend.mapper.IngredientMapper;
import com.food.manager.backend.repository.IngredientRepository;
import com.food.manager.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredientServiceTestSuite {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getIngredientSuccessfully() {
        Long ingredientId = 1L;
        Ingredient ingredient = new Ingredient(QuantityType.GRAM, 100, true, false, new Product("Flour"));
        ingredient.setIngredientId(ingredientId);

        IngredientResponse ingredientResponse = new IngredientResponse();

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toIngredientResponse(ingredient)).thenReturn(ingredientResponse);

        IngredientResponse result = ingredientService.getIngredient(ingredientId);

        assertNotNull(result);
        assertEquals(ingredientResponse, result);

        verify(ingredientRepository, times(1)).findById(ingredientId);
        verify(ingredientMapper, times(1)).toIngredientResponse(ingredient);
    }

    @Test
    void getIngredientThrowsIngredientNotFoundException() {
        Long ingredientId = 1L;

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        IngredientNotFoundException exception = assertThrows(IngredientNotFoundException.class, () -> {
            ingredientService.getIngredient(ingredientId);
        });

        assertEquals("Ingredient with ID: " + ingredientId + " not found", exception.getMessage());

        verify(ingredientRepository, times(1)).findById(ingredientId);
        verify(ingredientMapper, times(0)).toIngredientResponse(any(Ingredient.class));
    }

    @Test
    void getAllIngredientsSuccessfully() {
        Ingredient ingredient1 = new Ingredient(QuantityType.GRAM, 100, true, false, new Product("Flour"));
        Ingredient ingredient2 = new Ingredient(QuantityType.PIECE, 2, false, true, new Product("Egg"));

        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        IngredientResponse response1 = new IngredientResponse();
        IngredientResponse response2 = new IngredientResponse();

        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(ingredientMapper.toIngredientResponse(ingredient1)).thenReturn(response1);
        when(ingredientMapper.toIngredientResponse(ingredient2)).thenReturn(response2);

        List<IngredientResponse> result = ingredientService.getAllIngredients();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(ingredientRepository, times(1)).findAll();
        verify(ingredientMapper, times(1)).toIngredientResponse(ingredient1);
        verify(ingredientMapper, times(1)).toIngredientResponse(ingredient2);
    }

    @Test
    void getAllIngredientsReturnsEmptyListWhenNoIngredientsFound() {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        List<IngredientResponse> result = ingredientService.getAllIngredients();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(ingredientRepository, times(1)).findAll();
        verify(ingredientMapper, times(0)).toIngredientResponse(any(Ingredient.class));
    }

    @Test
    void createIngredientSuccessfully() {
        Long productId = 1L;
        CreateIngredientRequest request = new CreateIngredientRequest(QuantityType.GRAM, 100, true, false, productId);

        Product product = new Product("Flour");
        product.setProductId(productId);

        Ingredient ingredient = new Ingredient(QuantityType.GRAM, 100, true, false, product);
        IngredientResponse ingredientResponse = new IngredientResponse();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
        when(ingredientMapper.toIngredientResponse(ingredient)).thenReturn(ingredientResponse);

        IngredientResponse result = ingredientService.createIngredient(request);

        assertNotNull(result);
        assertEquals(ingredientResponse, result);

        verify(productRepository, times(1)).findById(productId);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
        verify(ingredientMapper, times(1)).toIngredientResponse(ingredient);
    }

    @Test
    void createIngredientThrowsProductNotFoundInProductsException() {
        Long productId = 1L;
        CreateIngredientRequest request = new CreateIngredientRequest(QuantityType.GRAM, 100, true, false, productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundInProductsException exception = assertThrows(ProductNotFoundInProductsException.class, () -> {
            ingredientService.createIngredient(request);
        });

        assertEquals("Product with ID: " + productId + " not found" , exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(ingredientRepository, times(0)).save(any(Ingredient.class));
        verify(ingredientMapper, times(0)).toIngredientResponse(any(Ingredient.class));
    }

    @Test
    void deleteIngredientSuccessfully() {
        Long ingredientId = 1L;

        when(ingredientRepository.existsById(ingredientId)).thenReturn(true);
        doNothing().when(ingredientRepository).deleteById(ingredientId);

        ingredientService.deleteIngredient(ingredientId);

        verify(ingredientRepository, times(1)).existsById(ingredientId);
        verify(ingredientRepository, times(1)).deleteById(ingredientId);
    }

    @Test
    void deleteIngredientThrowsIngredientNotFoundException() {
        Long ingredientId = 1L;

        when(ingredientRepository.existsById(ingredientId)).thenReturn(false);

        IngredientNotFoundException exception = assertThrows(IngredientNotFoundException.class, () -> {
            ingredientService.deleteIngredient(ingredientId);
        });

        assertEquals("Ingredient with ID: " + ingredientId + " not found", exception.getMessage());

        verify(ingredientRepository, times(1)).existsById(ingredientId);
        verify(ingredientRepository, times(0)).deleteById(ingredientId);
    }

    @Test
    void deleteAllIngredientsSuccessfully() {
        doNothing().when(ingredientRepository).deleteAll();

        ingredientService.deleteAllIngredients();

        verify(ingredientRepository, times(1)).deleteAll();
    }
}
