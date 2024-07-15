package com.food.manager.controller;

import com.food.manager.dto.request.fridge.AddProductRequest;
import com.food.manager.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.dto.request.fridgeproduct.AddFridgeProductRequest;
import com.food.manager.dto.response.FridgeProductResponse;
import com.food.manager.dto.response.FridgeResponse;
import com.food.manager.service.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fridges")
public class FridgeController {

    @Autowired
    private FridgeService fridgeService;

    @PostMapping("/addProduct")
    public ResponseEntity<FridgeProductResponse> addFridgeProduct(@RequestBody AddFridgeProductRequest addFridgeProductRequest) {
        FridgeProductResponse fridgeProductResponse = fridgeService.addFridgeProduct(addFridgeProductRequest);
        return ResponseEntity.ok(fridgeProductResponse);
    }

    @PutMapping("/addProduct/toFridge")
    public ResponseEntity<FridgeResponse> addProductToFridge(@RequestBody AddProductRequest addProductRequest) {
        FridgeResponse fridgeResponse = fridgeService.addProductToFridge(addProductRequest);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PutMapping("/removeProduct")
    public ResponseEntity<FridgeResponse> removeProduct(@RequestBody RemoveProductFromFridgeRequest removeProductRequest) {
        FridgeResponse fridgeResponse = fridgeService.removeProductFromFridge(removeProductRequest);
        return ResponseEntity.ok(fridgeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FridgeResponse> getFridge(@PathVariable Long id) {
        FridgeResponse fridgeResponse = fridgeService.getFridge(id);
        return ResponseEntity.ok(fridgeResponse);
    }

    @GetMapping
    public ResponseEntity<List<FridgeResponse>> getAllFridges() {
        List<FridgeResponse> fridges = fridgeService.getAllFridges();
        return ResponseEntity.ok(fridges);
    }
}
