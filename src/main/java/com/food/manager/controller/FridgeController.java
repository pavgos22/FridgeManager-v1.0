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

    @PutMapping("/addProduct")
    public ResponseEntity<FridgeProductResponse> addProduct(@RequestBody AddFridgeProductRequest addFridgeProductRequest) {
        FridgeProductResponse fridgeProductResponse = fridgeService.addFridgeProduct(addFridgeProductRequest);
        return ResponseEntity.ok(fridgeProductResponse);
    }

    @PostMapping("/removeProduct")
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
