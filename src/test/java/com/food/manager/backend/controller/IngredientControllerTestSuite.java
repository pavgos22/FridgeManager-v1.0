package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.ingredient.CreateIngredientRequest;
import com.food.manager.backend.dto.response.IngredientResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
class IngredientControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;

    private IngredientResponse ingredientResponse;

    @BeforeEach
    void setUp() {
        ingredientResponse = new IngredientResponse(1L, QuantityType.GRAM, 200, true, false, 101L, 1L);
    }

    @Test
    void getIngredientSuccessfully() throws Exception {
        Mockito.when(ingredientService.getIngredient(anyLong())).thenReturn(ingredientResponse);

        mockMvc.perform(get("/v1/ingredients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredientId").value(1L))
                .andExpect(jsonPath("$.quantityType").value("GRAM"))
                .andExpect(jsonPath("$.quantity").value(200))
                .andExpect(jsonPath("$.required").value(true))
                .andExpect(jsonPath("$.ignoreGroup").value(false))
                .andExpect(jsonPath("$.productId").value(101L))
                .andExpect(jsonPath("$.recipeId").value(1L));
    }

    @Test
    void getAllIngredientsSuccessfully() throws Exception {
        List<IngredientResponse> ingredients = Arrays.asList(ingredientResponse);

        Mockito.when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        mockMvc.perform(get("/v1/ingredients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ingredientId").value(1L))
                .andExpect(jsonPath("$[0].quantityType").value("GRAM"))
                .andExpect(jsonPath("$[0].quantity").value(200))
                .andExpect(jsonPath("$[0].required").value(true))
                .andExpect(jsonPath("$[0].ignoreGroup").value(false))
                .andExpect(jsonPath("$[0].productId").value(101L))
                .andExpect(jsonPath("$[0].recipeId").value(1L));
    }

    @Test
    void createIngredientSuccessfully() throws Exception {
        CreateIngredientRequest createIngredientRequest = new CreateIngredientRequest(QuantityType.GRAM, 200, true, false, 101L);

        Mockito.when(ingredientService.createIngredient(any(CreateIngredientRequest.class))).thenReturn(ingredientResponse);

        mockMvc.perform(post("/v1/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantityType\":\"GRAM\",\"quantity\":200,\"required\":true,\"ignoreGroup\":false,\"productId\":101}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredientId").value(1L))
                .andExpect(jsonPath("$.quantityType").value("GRAM"))
                .andExpect(jsonPath("$.quantity").value(200))
                .andExpect(jsonPath("$.required").value(true))
                .andExpect(jsonPath("$.ignoreGroup").value(false))
                .andExpect(jsonPath("$.productId").value(101L))
                .andExpect(jsonPath("$.recipeId").value(1L));
    }

    @Test
    void deleteIngredientSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/ingredients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllIngredientsSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/ingredients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
