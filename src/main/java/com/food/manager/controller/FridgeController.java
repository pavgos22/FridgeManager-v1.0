package com.food.manager.controller;

import com.food.manager.dto.request.fridge.AddProductToFridgeRequest;
import com.food.manager.dto.request.fridge.CreateFridgeRequest;
import com.food.manager.dto.request.fridge.RemoveProductFromFridgeRequest;
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

    @PostMapping
    public ResponseEntity<FridgeResponse> createFridge(@RequestBody CreateFridgeRequest createFridgeRequest) {
        FridgeResponse fridgeResponse = fridgeService.createFridge(createFridgeRequest);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<FridgeResponse> addProduct(@RequestBody AddProductToFridgeRequest addProductRequest) {
        FridgeResponse fridgeResponse = fridgeService.addProduct(addProductRequest);
        return ResponseEntity.ok(fridgeResponse);
    }

    @PostMapping("/removeProduct")
    public ResponseEntity<FridgeResponse> removeProduct(@RequestBody RemoveProductFromFridgeRequest removeProductRequest) {
        FridgeResponse fridgeResponse = fridgeService.removeProduct(removeProductRequest);
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
