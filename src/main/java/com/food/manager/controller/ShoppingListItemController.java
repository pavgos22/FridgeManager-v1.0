package com.food.manager.controller;

import com.food.manager.dto.response.ShoppingListItemResponse;
import com.food.manager.service.ShoppingListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/items")
public class ShoppingListItemController {

    @Autowired
    private ShoppingListItemService shoppingListItemService;

    @GetMapping
    public ResponseEntity<List<ShoppingListItemResponse>> getAllItems() {
        List<ShoppingListItemResponse> items = shoppingListItemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListItemResponse> getItem(@PathVariable Long id) {
        ShoppingListItemResponse item = shoppingListItemService.getItem(id);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        shoppingListItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllItems() {
        shoppingListItemService.deleteAllItems();
        return ResponseEntity.noContent().build();
    }
}
