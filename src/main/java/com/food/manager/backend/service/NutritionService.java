package com.food.manager.backend.service;

import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.entity.Nutrition;
import com.food.manager.backend.mapper.NutritionMapper;
import com.food.manager.backend.repository.NutritionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private NutritionMapper nutritionMapper;

    public List<NutritionResponse> getAllNutritions() {
        List<Nutrition> nutritionList = nutritionRepository.findAll();
        return nutritionList.stream().map(nutritionMapper::toNutritionResponse).toList();
    }

    public Optional<NutritionResponse> getNutrition(Long id) {
        return nutritionRepository.findById(id).map(nutritionMapper::toNutritionResponse);
    }
}
