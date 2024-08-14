package com.food.manager.backend.entity;

import com.food.manager.backend.enums.QuantityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTestSuite {

    private Validator validator;
    private ShoppingListItem shoppingListItem;
    private User author;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Product product = new Product("Test Product");
        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        shoppingListItem = new ShoppingListItem(product, QuantityType.PIECE, 10, false, group);
        author = new User("testUser", "Test", "User", "test@example.com", "password", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void shouldCreateValidComment() {
        Comment comment = new Comment("Test comment", LocalDateTime.now(), LocalDateTime.now(), shoppingListItem, author);

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertTrue(violations.isEmpty(), "Comment should be valid");

        assertEquals("Test comment", comment.getContent());
        assertNotNull(comment.getCreatedAt());
        assertNotNull(comment.getUpdatedAt());
        assertEquals(shoppingListItem, comment.getItem());
        assertEquals(author, comment.getAuthor());
    }

    @Test
    void shouldUpdateCommentContent() {
        Comment comment = new Comment("Original comment", LocalDateTime.now(), LocalDateTime.now(), shoppingListItem, author);

        comment.setContent("Updated comment");
        comment.setUpdatedAt(LocalDateTime.now());

        assertEquals("Updated comment", comment.getContent());
    }

    @Test
    void shouldMaintainRelationships() {
        Comment comment = new Comment("Test comment", LocalDateTime.now(), LocalDateTime.now(), shoppingListItem, author);

        assertEquals(shoppingListItem, comment.getItem());
        assertEquals(author, comment.getAuthor());
    }
}
