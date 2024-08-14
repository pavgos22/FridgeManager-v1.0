package com.food.manager.backend.entity;

import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateRecipe() {
        Recipe recipe = new Recipe("Pancakes", "Delicious pancakes", RecipeType.TEST, Weather.WARM, "http://example.com/pancakes");

        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertTrue(violations.isEmpty(), "Recipe should be valid with correct data");
    }

    @Test
    void shouldAddAndGetIngredients() {
        Recipe recipe = new Recipe("Pancakes", "Delicious pancakes", RecipeType.TEST, Weather.WARM, "http://example.com/pancakes");
        Product product = new Product("Milk");
        Ingredient ingredient = new Ingredient(QuantityType.LITER, 1, true, false, product);
        ingredient.setRecipe(recipe);
        recipe.getIngredients().add(ingredient);

        assertEquals(1, recipe.getIngredients().size(), "Recipe should have one ingredient");
        assertTrue(recipe.getIngredients().contains(ingredient), "Recipe should contain the added ingredient");
    }

    @Test
    void shouldRemoveIngredient() {
        Recipe recipe = new Recipe("Pancakes", "Delicious pancakes", RecipeType.TEST, Weather.WARM, "http://example.com/pancakes");
        Product product = new Product("Milk");
        Ingredient ingredient = new Ingredient(QuantityType.LITER, 1, true, false, product);
        ingredient.setRecipe(recipe);
        recipe.getIngredients().add(ingredient);

        assertEquals(1, recipe.getIngredients().size(), "Recipe should have one ingredient");

        recipe.getIngredients().remove(ingredient);
        assertEquals(0, recipe.getIngredients().size(), "Ingredient should be removed from the recipe");
    }
}
