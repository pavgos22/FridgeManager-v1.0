package com.food.manager.backend.service;

import com.food.manager.backend.dto.response.WishlistResponse;
import com.food.manager.backend.entity.Wishlist;
import com.food.manager.backend.mapper.WishlistMapper;
import com.food.manager.backend.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    private final WishlistMapper wishlistMapper;

    public WishlistService(WishlistMapper wishlistMapper) {
        this.wishlistMapper = wishlistMapper;
    }

    public WishlistResponse addProductToWishlist(String productName) {
        Wishlist wishlist = Wishlist.builder().productName(productName).build();

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toResponse(savedWishlist);
    }

    public List<Wishlist> getAllWishlistItems() {
        return new ArrayList<>(wishlistRepository.findAll());
    }

    public void clearWishlist() {
        wishlistRepository.deleteAll();
    }
}
