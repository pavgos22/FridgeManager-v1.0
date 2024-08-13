package com.food.manager.backend.service;

import com.food.manager.backend.dto.response.FridgeResponse;
import com.food.manager.backend.entity.*;
import com.food.manager.backend.exception.*;
import com.food.manager.backend.mapper.FridgeMapper;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.repository.FridgeProductRepository;
import com.food.manager.backend.repository.FridgeRepository;
import com.food.manager.backend.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ExecuteRecipeTestSuite {

    @Mock
    private FridgeRepository fridgeRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private FridgeProductRepository fridgeProductRepository;

    @Mock
    private FridgeMapper fridgeMapper;

    @InjectMocks
    private FridgeService fridgeService;

    private Fridge fridge;
    private Recipe recipe;
    private FridgeProduct fridgeProduct;
    private Ingredient ingredient;
    private FridgeResponse fridgeResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fridge = new Fridge();
        fridge.setFridgeId(1L);

        Product product = new Product();
        product.setProductName("Milk");

        fridgeProduct = new FridgeProduct();
        fridgeProduct.setProduct(product);
        fridgeProduct.setQuantity(2);
        fridgeProduct.setQuantityType(QuantityType.LITER);

        fridge.setProducts(List.of(fridgeProduct));

        ingredient = new Ingredient();
        ingredient.setProduct(product);
        ingredient.setQuantity(1);
        ingredient.setQuantityType(QuantityType.LITER);

        recipe = new Recipe();
        recipe.setIngredients(Set.of(ingredient));

        fridgeResponse = new FridgeResponse();
    }

    @Test
    void testExecuteRecipe_FridgeNotFound() {
        when(fridgeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FridgeNotFoundException.class, () -> fridgeService.executeRecipe(1L, 1L));

        verify(fridgeRepository, times(1)).findById(1L);
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    void testExecuteRecipe_RecipeNotFound() {
        when(fridgeRepository.findById(anyLong())).thenReturn(Optional.of(fridge));
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> fridgeService.executeRecipe(1L, 1L));

        verify(fridgeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).findById(1L);
    }

    @Test
    void testExecuteRecipe_MismatchedQuantityType() {
        ingredient.setQuantityType(QuantityType.GRAM);

        when(fridgeRepository.findById(anyLong())).thenReturn(Optional.of(fridge));
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        assertThrows(MismatchedQuantityTypeException.class, () -> fridgeService.executeRecipe(1L, 1L));

        verify(fridgeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).findById(1L);
    }

    @Test
    void testExecuteRecipe_InsufficientQuantity() {
        ingredient.setQuantity(3);

        when(fridgeRepository.findById(anyLong())).thenReturn(Optional.of(fridge));
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        assertThrows(InsufficientQuantityException.class, () -> fridgeService.executeRecipe(1L, 1L));

        verify(fridgeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).findById(1L);
    }

    @Test
    @Transactional
    void testExecuteRecipe_Success() {
        when(fridgeRepository.findById(anyLong())).thenReturn(Optional.of(fridge));
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));
        when(fridgeMapper.toFridgeResponse(any())).thenReturn(fridgeResponse);

        FridgeResponse result = fridgeService.executeRecipe(1L, 1L);

        assertNotNull(result);
        assertEquals(fridgeResponse, result);

        verify(fridgeRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).findById(1L);
        verify(fridgeProductRepository, times(1)).save(any(FridgeProduct.class));
        verify(fridgeMapper, times(1)).toFridgeResponse(any());
    }
}
