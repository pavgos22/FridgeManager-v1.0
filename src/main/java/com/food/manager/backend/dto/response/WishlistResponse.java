package com.food.manager.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WishlistResponse {
    private Long wishlistId;
    private String productName;
}
