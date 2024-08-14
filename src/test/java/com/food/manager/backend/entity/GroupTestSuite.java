package com.food.manager.backend.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GroupTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateGroup() {
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());

        Set<ConstraintViolation<Group>> violations = validator.validate(group);
        assertTrue(violations.isEmpty(), "Group should be valid with correct data");
    }

    @Test
    void shouldInitializeWithEmptyUserList() {
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        assertNotNull(group.getUsers(), "Users list should be initialized");
        assertTrue(group.getUsers().isEmpty(), "Users list should be empty initially");
    }

    @Test
    void shouldInitializeWithEmptyShoppingListItems() {
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        assertNotNull(group.getShoppingListItems(), "Shopping list items should be initialized");
        assertTrue(group.getShoppingListItems().isEmpty(), "Shopping list items should be empty initially");
    }
}

