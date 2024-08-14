package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.service.NutritionService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NutritionController.class)
class NutritionControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NutritionService nutritionService;

    private NutritionResponse nutritionResponse;

    @BeforeEach
    void setUp() {
        nutritionResponse = new NutritionResponse(1L, 500, 20.5f, 10.3f, 60.1f);
    }

    @Test
    void getAllNutritionsSuccessfully() throws Exception {
        List<NutritionResponse> nutritionList = Arrays.asList(nutritionResponse);

        Mockito.when(nutritionService.getAllNutritions()).thenReturn(nutritionList);

        mockMvc.perform(get("/v1/nutrition")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nutritionId").value(1L))
                .andExpect(jsonPath("$[0].calories").value(500))
                .andExpect(jsonPath("$[0].protein").value(20.5))
                .andExpect(jsonPath("$[0].fat").value(10.3))
                .andExpect(jsonPath("$[0].carbohydrate").value(60.1));
    }

    @Test
    void getNutritionSuccessfully() throws Exception {
        Mockito.when(nutritionService.getNutrition(anyLong())).thenReturn(Optional.of(nutritionResponse));

        mockMvc.perform(get("/v1/nutrition/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nutritionId").value(1L))
                .andExpect(jsonPath("$.calories").value(500))
                .andExpect(jsonPath("$.protein").value(20.5))
                .andExpect(jsonPath("$.fat").value(10.3))
                .andExpect(jsonPath("$.carbohydrate").value(60.1));
    }

    @Test
    void getNutritionNotFound() throws Exception {
        Mockito.when(nutritionService.getNutrition(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/nutrition/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}