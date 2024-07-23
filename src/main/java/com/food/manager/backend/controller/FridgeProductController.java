package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.FridgeProductResponse;
import com.food.manager.backend.service.FridgeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/fridgeProducts")
public class FridgeProductController {

    @Autowired
    private FridgeProductService fridgeProductService;

    @GetMapping("/{id}")
    public ResponseEntity<FridgeProductResponse> getFridgeProduct(@PathVariable Long id) {
        FridgeProductResponse fridgeProductResponse = fridgeProductService.getFridgeProduct(id);
        return ResponseEntity.ok(fridgeProductResponse);
    }

    @GetMapping
    public ResponseEntity<List<FridgeProductResponse>> getAllFridgeProducts() {
        List<FridgeProductResponse> fridgeProducts = fridgeProductService.getAllFridgeProducts();
        return ResponseEntity.ok(fridgeProducts);
    }
}
