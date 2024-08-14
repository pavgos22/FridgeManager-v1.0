package com.food.manager.backend.entity;

import com.food.manager.backend.enums.QuantityType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FridgeTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateFridgeWithValidGroup() {
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        Fridge fridge = new Fridge(group);

        Set<ConstraintViolation<Fridge>> violations = validator.validate(fridge);
        assertTrue(violations.isEmpty(), "Fridge should be valid when created with a valid Group");
    }

    @Test
    void shouldAddFridgeProductToFridge() {
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        Fridge fridge = new Fridge(group);

        Product product = new Product("Test Product");
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);
        fridge.getProducts().add(fridgeProduct);

        assertEquals(1, fridge.getProducts().size());
        assertTrue(fridge.getProducts().contains(fridgeProduct));
    }

    @Test
    void shouldRemoveFridgeProductFromFridge() {
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        Fridge fridge = new Fridge(group);

        Product product = new Product("Test Product");
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);
        fridge.getProducts().add(fridgeProduct);

        fridge.getProducts().remove(fridgeProduct);

        assertEquals(0, fridge.getProducts().size());
        assertFalse(fridge.getProducts().contains(fridgeProduct));
    }
}

