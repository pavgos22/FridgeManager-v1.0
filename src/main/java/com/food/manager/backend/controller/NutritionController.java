package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/nutrition")
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;

    @GetMapping
    public ResponseEntity<List<NutritionResponse>> getAllNutritions() {
        List<NutritionResponse> nutritionList = nutritionService.getAllNutritions();
        return ResponseEntity.ok(nutritionList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionResponse> getNutrition(@PathVariable Long id) {
        return nutritionService.getNutrition(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
