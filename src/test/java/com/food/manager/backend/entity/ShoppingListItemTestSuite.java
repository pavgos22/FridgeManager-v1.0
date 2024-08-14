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

class ShoppingListItemTestSuite {

    private Validator validator;
    private Product product;
    private Group group;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        product = new Product("Milk");
        group = new Group("Test Group");
    }

    @Test
    void shouldCreateShoppingListItem() {
        ShoppingListItem item = new ShoppingListItem(product, QuantityType.LITER, 2, false, group);

        Set<ConstraintViolation<ShoppingListItem>> violations = validator.validate(item);
        assertTrue(violations.isEmpty(), "ShoppingListItem should be valid with correct data");
    }

    @Test
    void shouldAddAndRemoveComments() {
        ShoppingListItem item = new ShoppingListItem(product, QuantityType.LITER, 2, false, group);
        Comment comment = new Comment("Test comment", null, null, item, null);

        item.getComments().add(comment);
        assertEquals(1, item.getComments().size(), "Item should have one comment");

        item.getComments().remove(comment);
        assertEquals(0, item.getComments().size(), "Item should have no comments after removal");
    }

    @Test
    void shouldMarkAsChecked() {
        ShoppingListItem item = new ShoppingListItem(product, QuantityType.LITER, 2, false, group);
        item.setChecked(true);
        assertTrue(item.isChecked(), "Item should be marked as checked");
    }

    @Test
    void shouldSetAndGetQuantityType() {
        ShoppingListItem item = new ShoppingListItem(product, QuantityType.LITER, 2, false, group);
        item.setQuantityType(QuantityType.PIECE);
        assertEquals(QuantityType.PIECE, item.getQuantityType(), "Quantity type should be set correctly");
    }
}
