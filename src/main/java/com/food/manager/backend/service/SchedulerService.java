package com.food.manager.backend.service;

import com.food.manager.backend.entity.Product;
import com.food.manager.backend.entity.Wishlist;
import com.food.manager.backend.exception.ProductNotFoundInProductsException;
import com.food.manager.backend.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductService productService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void processWishlist() {
        List<Wishlist> wishlistItems = wishlistRepository.findAll();
        for (Wishlist wishlistItem : wishlistItems) {
            try {
                productService.createProductFromWishlist(wishlistItem.getProductName());
            } catch (ProductNotFoundInProductsException e) {
                System.out.println(e.getMessage());
            }
        }
        wishlistRepository.deleteAll();
    }
}
