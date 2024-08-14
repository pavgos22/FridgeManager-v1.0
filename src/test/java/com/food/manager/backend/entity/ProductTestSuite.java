package com.food.manager.backend.entity;

import com.food.manager.backend.enums.ProductGroup;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductTestSuite {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateProductWithValidData() {
        Product product = new Product("Milk", ProductGroup.TEST);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty(), "Product should be valid with correct data");
    }

    @Test
    void shouldSetAndGetNutritionCorrectly() {
        Nutrition nutrition = new Nutrition(100, 10.0f, 5.0f, 20.0f);
        Product product = new Product("Milk", ProductGroup.TEST);
        product.setNutrition(nutrition);

        assertEquals(nutrition, product.getNutrition(), "Nutrition should be correctly set and retrieved");
    }

    @Test
    void shouldAddAndGetIngredientsCorrectly() {
        Product product = new Product("Milk", ProductGroup.TEST);
        Ingredient ingredient = new Ingredient(QuantityType.LITER, 1, true, false, product);
        product.getIngredients().add(ingredient);

        assertEquals(1, product.getIngredients().size(), "Product should have one ingredient");
        assertTrue(product.getIngredients().contains(ingredient), "Product should contain the added ingredient");
    }

    @Test
    void shouldGetRecipeIdsCorrectly() {
        Product product = new Product("Milk", ProductGroup.TEST);
        Recipe recipe = new Recipe("Pancakes", "Delicious pancakes", RecipeType.TEST, Weather.WARM, "http://example.com/pancakes");
        Ingredient ingredient = new Ingredient(QuantityType.LITER, 1, true, false, product);
        ingredient.setRecipe(recipe);
        product.getIngredients().add(ingredient);

        List<Long> recipeIds = product.getRecipeIds();
        assertEquals(1, recipeIds.size(), "There should be one recipe ID");
        assertEquals(recipe.getRecipeId(), recipeIds.getFirst(), "The recipe ID should match the ID of the associated recipe");
    }

    @Test
    void shouldHandleEqualsAndHashCodeCorrectly() {
        Product product1 = new Product("Milk", ProductGroup.TEST);
        Product product2 = new Product("Milk", ProductGroup.TEST);

        assertEquals(product1, product2, "Products with the same name and group should be equal");
        assertEquals(product1.hashCode(), product2.hashCode(), "Products with the same name and group should have the same hash code");
    }

    @Test
    void shouldRemoveFridgeProductsCorrectly() {
        Product product = new Product("Milk", ProductGroup.TEST);
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.LITER, 1, new Fridge(new Group("Test Group")), product);
        product.getFridgeProducts().add(fridgeProduct);

        assertEquals(1, product.getFridgeProducts().size(), "Product should have one fridge product");

        product.getFridgeProducts().remove(fridgeProduct);

        assertEquals(0, product.getFridgeProducts().size(), "Fridge product should be removed");
    }
}
