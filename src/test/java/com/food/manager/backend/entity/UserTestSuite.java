package com.food.manager.backend.entity;

import com.food.manager.backend.enums.QuantityType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateUser() {
        User user = new User("testuser", "John", "Doe", "john.doe@example.com", "password123", LocalDateTime.now(), LocalDateTime.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "User should be valid with correct data");
    }


    @Test
    void shouldAddAndRemoveGroups() {
        User user = new User("testuser", "John", "Doe", "john.doe@example.com", "password123", LocalDateTime.now(), LocalDateTime.now());
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());

        user.getGroups().add(group);
        assertEquals(1, user.getGroups().size(), "User should have one group");

        user.getGroups().remove(group);
        assertEquals(0, user.getGroups().size(), "User should have no groups after removal");
    }

    @Test
    void shouldAddAndRemoveComments() {
        User user = new User("testuser", "John", "Doe", "john.doe@example.com", "password123", LocalDateTime.now(), LocalDateTime.now());
        ShoppingListItem item = new ShoppingListItem(new Product("Milk"), QuantityType.LITER, 2, false, new Group("Test Group"));
        Comment comment = new Comment("Test comment", LocalDateTime.now(), LocalDateTime.now(), item, user);

        user.getComments().add(comment);
        assertEquals(1, user.getComments().size(), "User should have one comment");

        user.getComments().remove(comment);
        assertEquals(0, user.getComments().size(), "User should have no comments after removal");
    }

    @Test
    void shouldUpdateUserDetails() {
        User user = new User("testuser", "John", "Doe", "john.doe@example.com", "password123", LocalDateTime.now(), LocalDateTime.now());

        user.setUsername("updateduser");
        user.setFirstName("Updated");
        user.setLastName("User");
        user.setEmail("updated.email@example.com");

        assertEquals("updateduser", user.getUsername());
        assertEquals("Updated", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("updated.email@example.com", user.getEmail());
    }
}
