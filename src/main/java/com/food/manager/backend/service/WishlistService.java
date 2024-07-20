package com.food.manager.backend.service;

import com.food.manager.backend.entity.Wishlist;
import com.food.manager.backend.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public Wishlist addProductToWishlist(String productName) {
        Wishlist wishlist = new Wishlist(productName);
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getAllWishlistItems() {
        return wishlistRepository.findAll();
    }

    public void clearWishlist() {
        wishlistRepository.deleteAll();
    }
}
