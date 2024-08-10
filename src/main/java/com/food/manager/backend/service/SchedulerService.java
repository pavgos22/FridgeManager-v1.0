package com.food.manager.backend.service;

import com.food.manager.backend.config.DbManager;
import com.food.manager.backend.entity.Wishlist;
import com.food.manager.backend.exception.ProductNotFoundInProductsException;
import com.food.manager.backend.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DbManager.getInstance().getConnection();

            for (Wishlist wishlistItem : wishlistItems) {
                try {
                    productService.createProductFromWishlist(wishlistItem.getProductName());

                    String query = "INSERT INTO wishlist (product_name) VALUES (?)";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, wishlistItem.getProductName());
                    preparedStatement.executeUpdate();

                } catch (ProductNotFoundInProductsException e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        wishlistRepository.deleteAll();
    }
}
