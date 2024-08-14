package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.fridge.AddProductRequest;
import com.food.manager.backend.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.backend.dto.response.FridgeProductResponse;
import com.food.manager.backend.dto.response.FridgeResponse;
import com.food.manager.backend.dto.response.IngredientResponse;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.service.FridgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FridgeController.class)
class FridgeControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FridgeService fridgeService;

    private FridgeResponse fridgeResponse;
    private FridgeProductResponse fridgeProductResponse;
    private RecipeResponse recipeResponse;

    @BeforeEach
    void setUp() {
        fridgeProductResponse = new FridgeProductResponse(
                1L,
                QuantityType.PIECE,
                10,
                "Milk"
        );

        fridgeResponse = new FridgeResponse(
                1L,
                2L,
                Arrays.asList(fridgeProductResponse)
        );

        recipeResponse = new RecipeResponse();
        recipeResponse.setRecipeId(1L);
        recipeResponse.setRecipeName("Pancake");

        IngredientResponse milk = new IngredientResponse(1L, QuantityType.LITER, 1, true, false, 101L, 1L);
        IngredientResponse flour = new IngredientResponse(2L, QuantityType.GRAM, 200, true, false, 102L, 1L);
        IngredientResponse egg = new IngredientResponse(3L, QuantityType.PIECE, 2, true, false, 103L, 1L);

        recipeResponse.setIngredients(Set.of(milk, flour, egg));
    }

    @Test
    void getAllFridgesSuccessfully() throws Exception {
        List<FridgeResponse> fridgeResponses = Arrays.asList(fridgeResponse);

        when(fridgeService.getAllFridges()).thenReturn(fridgeResponses);

        mockMvc.perform(get("/v1/fridges")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fridgeId").value(1L))
                .andExpect(jsonPath("$[0].group").value(2L))
                .andExpect(jsonPath("$[0].fridgeProducts[0].fridgeProductId").value(1L))
                .andExpect(jsonPath("$[0].fridgeProducts[0].productName").value("Milk"))
                .andExpect(jsonPath("$[0].fridgeProducts[0].quantity").value(10))
                .andExpect(jsonPath("$[0].fridgeProducts[0].quantityType").value("PIECE"));
    }

    @Test
    void getFridgeSuccessfully() throws Exception {
        when(fridgeService.getFridge(anyLong())).thenReturn(fridgeResponse);

        mockMvc.perform(get("/v1/fridges/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fridgeId").value(1L))
                .andExpect(jsonPath("$.group").value(2L))
                .andExpect(jsonPath("$.fridgeProducts[0].fridgeProductId").value(1L))
                .andExpect(jsonPath("$.fridgeProducts[0].productName").value("Milk"))
                .andExpect(jsonPath("$.fridgeProducts[0].quantity").value(10))
                .andExpect(jsonPath("$.fridgeProducts[0].quantityType").value("PIECE"));
    }

    @Test
    void addProductToFridgeSuccessfully() throws Exception {
        when(fridgeService.addProductToFridge(anyLong(), any(AddProductRequest.class))).thenReturn(fridgeResponse);

        mockMvc.perform(put("/v1/fridges/{id}/addProduct", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 1, \"quantity\": 5, \"quantityType\": \"PIECE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fridgeId").value(1L))
                .andExpect(jsonPath("$.fridgeProducts[0].fridgeProductId").value(1L))
                .andExpect(jsonPath("$.fridgeProducts[0].productName").value("Milk"))
                .andExpect(jsonPath("$.fridgeProducts[0].quantity").value(10))
                .andExpect(jsonPath("$.fridgeProducts[0].quantityType").value("PIECE"));
    }

    @Test
    void removeProductFromFridgeSuccessfully() throws Exception {
        when(fridgeService.removeProductFromFridge(anyLong(), any(RemoveProductFromFridgeRequest.class))).thenReturn(fridgeResponse);

        mockMvc.perform(put("/v1/fridges/{id}/removeProduct", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fridgeProductId\": 1, \"quantity\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fridgeId").value(1L))
                .andExpect(jsonPath("$.fridgeProducts[0].fridgeProductId").value(1L))
                .andExpect(jsonPath("$.fridgeProducts[0].productName").value("Milk"))
                .andExpect(jsonPath("$.fridgeProducts[0].quantity").value(10))
                .andExpect(jsonPath("$.fridgeProducts[0].quantityType").value("PIECE"));
    }

    @Test
    void getRecipesPossibleWithFridgeProductsSuccessfully() throws Exception {
        List<RecipeResponse> recipes = Arrays.asList(recipeResponse);

        when(fridgeService.getRecipesPossibleWithFridgeProducts(anyLong())).thenReturn(recipes);

        mockMvc.perform(get("/v1/fridges/recipes/{fridgeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeId").value(1L))
                .andExpect(jsonPath("$[0].recipeName").value("Pancake"))
                .andExpect(jsonPath("$[0].ingredients[0].productId").value(103L))
                .andExpect(jsonPath("$[0].ingredients[1].productId").value(101L))
                .andExpect(jsonPath("$[0].ingredients[2].productId").value(102L));
    }


    @Test
    void getFridgeProductsSuccessfully() throws Exception {
        List<FridgeProductResponse> fridgeProducts = Arrays.asList(fridgeProductResponse);

        when(fridgeService.getFridgeProducts(anyLong())).thenReturn(fridgeProducts);

        mockMvc.perform(get("/v1/fridges/{fridgeId}/products", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fridgeProductId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Milk"))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[0].quantityType").value("PIECE"));
    }

    @Test
    void executeRecipeSuccessfully() throws Exception {
        when(fridgeService.executeRecipe(anyLong(), anyLong())).thenReturn(fridgeResponse);

        mockMvc.perform(put("/v1/fridges/{fridgeId}/executeRecipe/{recipeId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fridgeId").value(1L))
                .andExpect(jsonPath("$.group").value(2L))
                .andExpect(jsonPath("$.fridgeProducts[0].fridgeProductId").value(1L))
                .andExpect(jsonPath("$.fridgeProducts[0].productName").value("Milk"))
                .andExpect(jsonPath("$.fridgeProducts[0].quantity").value(10))
                .andExpect(jsonPath("$.fridgeProducts[0].quantityType").value("PIECE"));
    }
}
