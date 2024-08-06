package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.item.AddItemToListRequest;
import com.food.manager.backend.dto.request.item.RemoveItemFromListRequest;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.service.ShoppingListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/{id}/comments")
    public ResponseEntity<Map<String, String>> getItemComments(@PathVariable Long id) {
        Map<String, String> comments = shoppingListItemService.getItemComments(id);
        return ResponseEntity.ok(comments);
    }

    @PutMapping
    public ResponseEntity<ShoppingListItemResponse> addItemToShoppingList(@RequestBody AddItemToListRequest addItemToListRequest) {
        ShoppingListItemResponse itemResponse = shoppingListItemService.addItemToShoppingList(addItemToListRequest);
        return ResponseEntity.ok(itemResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeItemFromShoppingList(@RequestBody RemoveItemFromListRequest removeItemFromListRequest) {
        shoppingListItemService.removeItemFromShoppingList(removeItemFromListRequest);
        return ResponseEntity.noContent().build();
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
