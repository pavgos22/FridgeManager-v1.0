package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.backend.dto.response.RecipeNutrition;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.entity.Ingredient;
import com.food.manager.backend.entity.Nutrition;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.entity.Recipe;
import com.food.manager.backend.enums.ProductGroup;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import com.food.manager.backend.exception.IngredientNotFoundException;
import com.food.manager.backend.exception.RecipeNotFoundException;
import com.food.manager.backend.repository.IngredientRepository;
import com.food.manager.backend.repository.NutritionRepository;
import com.food.manager.backend.repository.ProductRepository;
import com.food.manager.backend.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecipeServiceTestSuite {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    private Recipe existingRecipe;

    private Recipe recipe;
    private Product product1;
    private Product product2;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void setUp() {
        existingRecipe = recipeRepository.save(new Recipe("Test Recipe", "Delicious test recipe", RecipeType.TEST, Weather.HOT, "http://test-recipe.url"));
        Product product1 = new Product("Product 1", ProductGroup.TEST);
        Product product2 = new Product("Product 2", ProductGroup.TEST);
        productRepository.saveAll(Arrays.asList(product1, product2));

        ingredient1 = new Ingredient(QuantityType.GRAM, 100, true, false, product1);
        ingredient2 = new Ingredient(QuantityType.GRAM, 200, true, false, product2);
        ingredientRepository.saveAll(Arrays.asList(ingredient1, ingredient2));
    }

    @Test
    void getRecipeSuccessfully() {
        RecipeResponse response = recipeService.getRecipe(existingRecipe.getRecipeId());

        assertNotNull(response);
        assertEquals(existingRecipe.getRecipeName(), response.getRecipeName());
        assertEquals(existingRecipe.getRecipeType(), response.getRecipeType());
    }

    @Test
    void getRecipeThrowsRecipeNotFoundException() {
        Long invalidRecipeId = 420420L;

        RuntimeException exception = assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.getRecipe(invalidRecipeId);
        });

        assertEquals("Recipe not found with id: " + invalidRecipeId, exception.getMessage());
    }

    @Test
    void getAllRecipesSuccessfully() {
        List<RecipeResponse> responses = recipeService.getAllRecipes();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());

        RecipeResponse response = responses.getLast();
        assertEquals(existingRecipe.getRecipeName(), response.getRecipeName());
        assertEquals(existingRecipe.getRecipeType(), response.getRecipeType());
    }

    @Test
    void createRecipeSuccessfully() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "New Recipe",
                "A new delicious recipe",
                RecipeType.TEST,
                Weather.HOT,
                Set.of(ingredient1.getIngredientId(), ingredient2.getIngredientId()),
                "http://new-recipe.url"
        );

        RecipeResponse response = recipeService.createRecipe(request);

        assertNotNull(response);
        assertEquals("New Recipe", response.getRecipeName());
        assertEquals(2, response.getIngredients().size());

        Recipe savedRecipe = recipeRepository.findById(response.getRecipeId()).orElse(null);
        assertNotNull(savedRecipe);
        assertEquals("New Recipe", savedRecipe.getRecipeName());
        assertEquals(2, savedRecipe.getIngredients().size());

        Set<Ingredient> ingredients = savedRecipe.getIngredients();
        assertTrue(ingredients.stream().allMatch(ingredient -> ingredient.getRecipe().equals(savedRecipe)));
    }

    @Test
    void createRecipeThrowsIngredientNotFoundException() {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "New Recipe",
                "A new delicious recipe",
                RecipeType.TEST,
                Weather.HOT,
                Set.of(ingredient1.getIngredientId(), 420420L),
                "http://new-recipe.url"
        );

        IngredientNotFoundException exception = assertThrows(IngredientNotFoundException.class, () -> {
            recipeService.createRecipe(request);
        });

        assertEquals("One or more ingredients not found", exception.getMessage());
    }

    @Test
    void getRecipesForCurrentWeatherSuccessfully() {
        Weather currentWeather = recipeService.fetchWeatherFromApi();

        Recipe weatherRecipe = new Recipe("Test recipe", "description", RecipeType.TEST, currentWeather, "url");
        recipeRepository.save(weatherRecipe);

        List<RecipeResponse> responses = recipeService.getRecipesForCurrentWeather();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());

        responses.forEach(response -> assertEquals(currentWeather, response.getWeather()));

        recipeRepository.delete(weatherRecipe);
    }
}
