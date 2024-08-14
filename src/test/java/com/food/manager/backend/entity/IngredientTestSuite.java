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

class IngredientTestSuite {

    private Validator validator;
    private Product product;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        product = new Product("Test Product");
        recipe = new Recipe("Test Recipe", "Description", null, null, null);
    }

    @Test
    void shouldCreateIngredient() {
        Ingredient ingredient = new Ingredient(QuantityType.PIECE, 5, true, false, product);

        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertTrue(violations.isEmpty(), "Ingredient should be valid with correct data");
    }


    @Test
    void shouldSetAndGetRecipeCorrectly() {
        Ingredient ingredient = new Ingredient(QuantityType.PIECE, 5, true, false, product);
        ingredient.setRecipe(recipe);
        assertEquals(recipe, ingredient.getRecipe(), "Recipe should be correctly set and retrieved");
    }
}

