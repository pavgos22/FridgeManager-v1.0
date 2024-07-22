package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.WishlistResponse;
import com.food.manager.backend.entity.Wishlist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistMapper {
    public WishlistResponse toResponse(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }
        return new WishlistResponse(
                wishlist.getId(),
                wishlist.getProductName()
        );
    }

    public List<WishlistResponse> mapToWishlistList(List<Wishlist> wishlists) {
        return wishlists.stream()
                .map(this::toResponse)
                .toList();
    }
}
