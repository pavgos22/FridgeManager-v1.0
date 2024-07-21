package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.WishlistResponse;
import com.food.manager.backend.entity.Wishlist;

public class WishlistMapper {

    public static WishlistResponse toResponse(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }
        return new WishlistResponse(wishlist.getId(), wishlist.getProductName());
    }

    public static Wishlist toEntity(WishlistResponse wishlistResponse) {
        if (wishlistResponse == null) {
            return null;
        }
        Wishlist wishlist = new Wishlist(wishlistResponse.productName());
        wishlist.setId(wishlistResponse.wishlistId());
        return wishlist;
    }
}
