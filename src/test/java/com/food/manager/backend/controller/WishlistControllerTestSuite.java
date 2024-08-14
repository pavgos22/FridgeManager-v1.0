package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.WishlistResponse;
import com.food.manager.backend.entity.Wishlist;
import com.food.manager.backend.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishlistController.class)
class WishlistControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    private WishlistResponse wishlistResponse;
    private Wishlist wishlistItem;

    @BeforeEach
    void setUp() {
        wishlistResponse = new WishlistResponse(1L, "Test Product");
        wishlistItem = new Wishlist(1L, "Test Product");
    }

    @Test
    void addProductToWishlistSuccessfully() throws Exception {
        Mockito.when(wishlistService.addProductToWishlist(anyString())).thenReturn(wishlistResponse);

        mockMvc.perform(post("/v1/wishlist")
                        .param("productName", "Test Product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wishlistId").value(1L))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    void getAllWishlistItemsSuccessfully() throws Exception {
        List<Wishlist> wishlistItems = Arrays.asList(wishlistItem);
        Mockito.when(wishlistService.getAllWishlistItems()).thenReturn(wishlistItems);

        mockMvc.perform(get("/v1/wishlist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }


    @Test
    void clearWishlistSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/wishlist/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(wishlistService, Mockito.times(1)).clearWishlist();
    }
}
