package com.food.manager.backend.service;

import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.entity.Nutrition;
import com.food.manager.backend.mapper.NutritionMapper;
import com.food.manager.backend.repository.NutritionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NutritionServiceTest {

    @Mock
    private NutritionRepository nutritionRepository;

    @Mock
    private NutritionMapper nutritionMapper;

    @InjectMocks
    private NutritionService nutritionService;

    private Nutrition nutrition;
    private NutritionResponse nutritionResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        nutrition = new Nutrition(200, 15.0f, 5.0f, 30.0f);
        nutrition.setNutritionId(1L);

        nutritionResponse = new NutritionResponse(1L, 200, 15.0f, 5.0f, 30.0f);
    }

    @Test
    void testGetAllNutritions() {
        List<Nutrition> nutritionList = Arrays.asList(nutrition);
        List<NutritionResponse> expectedResponses = Arrays.asList(nutritionResponse);

        when(nutritionRepository.findAll()).thenReturn(nutritionList);
        when(nutritionMapper.toNutritionResponse(nutrition)).thenReturn(nutritionResponse);

        List<NutritionResponse> result = nutritionService.getAllNutritions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedResponses, result);

        verify(nutritionRepository, times(1)).findAll();
        verify(nutritionMapper, times(1)).toNutritionResponse(nutrition);
    }

    @Test
    void testGetNutritionById_Found() {
        when(nutritionRepository.findById(1L)).thenReturn(Optional.of(nutrition));
        when(nutritionMapper.toNutritionResponse(nutrition)).thenReturn(nutritionResponse);

        Optional<NutritionResponse> result = nutritionService.getNutrition(1L);

        assertTrue(result.isPresent());
        assertEquals(nutritionResponse, result.get());

        verify(nutritionRepository, times(1)).findById(1L);
        verify(nutritionMapper, times(1)).toNutritionResponse(nutrition);
    }

    @Test
    void testGetNutritionById_NotFound() {
        when(nutritionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<NutritionResponse> result = nutritionService.getNutrition(1L);

        assertFalse(result.isPresent());

        verify(nutritionRepository, times(1)).findById(1L);
        verify(nutritionMapper, never()).toNutritionResponse(any(Nutrition.class));
    }
}
