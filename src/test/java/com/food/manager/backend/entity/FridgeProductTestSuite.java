package com.food.manager.backend.entity;

import com.food.manager.backend.enums.QuantityType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FridgeProductTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateFridgeProduct() {
        Fridge fridge = new Fridge(new Group("Test Group"));
        Product product = new Product("Test Product");
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);

        Set<ConstraintViolation<FridgeProduct>> violations = validator.validate(fridgeProduct);
        assertTrue(violations.isEmpty(), "FridgeProduct should be valid with correct data");
    }
}

