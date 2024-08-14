package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.backend.dto.response.RecipeNutrition;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import com.food.manager.backend.service.RecipeService;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    private RecipeResponse recipeResponse;
    private RecipeNutrition recipeNutrition;

    @BeforeEach
    void setUp() {
        recipeResponse = new RecipeResponse(1L, "Pancake", "Delicious pancake", RecipeType.TEST, Weather.WARM, Set.of(), "http://recipeurl.com");
        recipeNutrition = new RecipeNutrition(300, 10.0f, 5.0f, 50.0f);
    }

    @Test
    void createRecipeSuccessfully() throws Exception {
        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest("Pancake", "Delicious pancake", RecipeType.TEST, Weather.WARM, Set.of(1L, 2L), "http://recipeurl.com");

        Mockito.when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(recipeResponse);

        mockMvc.perform(post("/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipeName\":\"Pancake\",\"description\":\"Delicious pancake\",\"recipeType\":\"TEST\",\"weather\":\"WARM\",\"ingredientIds\":[1, 2],\"recipeURL\":\"http://recipeurl.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(1L))
                .andExpect(jsonPath("$.recipeName").value("Pancake"))
                .andExpect(jsonPath("$.description").value("Delicious pancake"))
                .andExpect(jsonPath("$.recipeType").value("TEST"))
                .andExpect(jsonPath("$.weather").value("WARM"))
                .andExpect(jsonPath("$.recipeUrl").value("http://recipeurl.com"));
    }

    @Test
    void getRecipeSuccessfully() throws Exception {
        Mockito.when(recipeService.getRecipe(anyLong())).thenReturn(recipeResponse);

        mockMvc.perform(get("/v1/recipes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(1L))
                .andExpect(jsonPath("$.recipeName").value("Pancake"))
                .andExpect(jsonPath("$.description").value("Delicious pancake"));
    }

    @Test
    void getRecipeNutritionSuccessfully() throws Exception {
        Mockito.when(recipeService.calcNutrition(anyLong())).thenReturn(recipeNutrition);

        mockMvc.perform(get("/v1/recipes/{id}/nutrition", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calories").value(300))
                .andExpect(jsonPath("$.protein").value(10.0f))
                .andExpect(jsonPath("$.fat").value(5.0f))
                .andExpect(jsonPath("$.carbohydrate").value(50.0f));
    }

    @Test
    void getAllRecipesSuccessfully() throws Exception {
        List<RecipeResponse> recipes = Arrays.asList(recipeResponse);
        Mockito.when(recipeService.getAllRecipes()).thenReturn(recipes);

        mockMvc.perform(get("/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeId").value(1L))
                .andExpect(jsonPath("$[0].recipeName").value("Pancake"));
    }

    @Test
    void getAllRecipesForWeatherSuccessfully() throws Exception {
        List<RecipeResponse> recipes = Arrays.asList(recipeResponse);
        Mockito.when(recipeService.getRecipesForCurrentWeather()).thenReturn(recipes);

        mockMvc.perform(get("/v1/recipes/weather")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeId").value(1L))
                .andExpect(jsonPath("$[0].recipeName").value("Pancake"));
    }

    @Test
    void deleteRecipeSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/recipes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllRecipesSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
