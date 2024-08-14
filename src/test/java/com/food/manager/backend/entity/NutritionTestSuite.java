package com.food.manager.backend.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NutritionTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateNutrition() {
        Nutrition nutrition = new Nutrition(200, 10.0f, 5.0f, 30.0f);

        Set<ConstraintViolation<Nutrition>> violations = validator.validate(nutrition);
        assertTrue(violations.isEmpty(), "Nutrition should be valid with correct data");
    }

    @Test
    void shouldSetAndGetProduct() {
        Product product = new Product("Test Product");
        Nutrition nutrition = new Nutrition(200, 10.0f, 5.0f, 30.0f);
        nutrition.setProduct(product);
        assertEquals(product, nutrition.getProduct(), "Product should be correctly set and retrieved");
    }
}

