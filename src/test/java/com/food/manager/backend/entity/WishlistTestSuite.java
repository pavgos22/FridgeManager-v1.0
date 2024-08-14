package com.food.manager.backend.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WishlistTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateWishlist() {
        Wishlist wishlist = new Wishlist("Product Name");

        Set<ConstraintViolation<Wishlist>> violations = validator.validate(wishlist);
        assertTrue(violations.isEmpty(), "Wishlist should be valid with correct data");
    }

    @Test
    void shouldUpdateProductName() {
        Wishlist wishlist = new Wishlist("Old Product Name");

        wishlist.setProductName("New Product Name");

        assertEquals("New Product Name", wishlist.getProductName());
    }
}
