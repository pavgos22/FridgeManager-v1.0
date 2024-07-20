package com.food.manager.backend.controller;

import com.food.manager.backend.entity.Wishlist;
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

    @PostMapping("/add")
    public ResponseEntity<Wishlist> addProductToWishlist(@RequestParam String productName) {
        Wishlist wishlist = wishlistService.addProductToWishlist(productName);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping
    public ResponseEntity<List<Wishlist>> getAllWishlistItems() {
        List<Wishlist> wishlist = wishlistService.getAllWishlistItems();
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearWishlist() {
        wishlistService.clearWishlist();
        return ResponseEntity.noContent().build();
    }
}
