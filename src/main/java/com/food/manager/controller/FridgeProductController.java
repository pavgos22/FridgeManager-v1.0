package com.food.manager.controller;

import com.food.manager.dto.request.fridgeproduct.AddFridgeProductRequest;
import com.food.manager.dto.response.FridgeProductResponse;
import com.food.manager.service.FridgeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
