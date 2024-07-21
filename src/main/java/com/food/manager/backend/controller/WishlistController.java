package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.WishlistResponse;
import com.food.manager.backend.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<WishlistResponse> addProductToWishlist(@RequestParam String productName) {
        WishlistResponse wishlistResponse = wishlistService.addProductToWishlist(productName);
        return ResponseEntity.ok(wishlistResponse);
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getAllWishlistItems() {
        List<WishlistResponse> wishlist = wishlistService.getAllWishlistItems();
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearWishlist() {
        wishlistService.clearWishlist();
        return ResponseEntity.noContent().build();
    }
}

