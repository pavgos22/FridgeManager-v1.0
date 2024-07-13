package com.food.manager.entity;

import com.food.manager.enums.RecipeType;
import com.food.manager.enums.Weather;
import com.food.manager.repository.ProductRepository;
import com.food.manager.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RecipeTestSuite {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ProductRepository productRepository;

    private Recipe recipe;
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product("TestProduct");
        productRepository.save(product);

        recipe = new Recipe();
        recipe.setDescription("Test Description");
        recipe.setNumberOfServings(4);
        recipe.setRecipeType(RecipeType.AMERICAN);
        recipe.setWeather(Weather.SUNNY);
        recipe.setProducts(new ArrayList<>(List.of(product)));
    }

    @Test
    @Transactional
    public void testCreateRecipe() {
        // When
        recipeRepository.save(recipe);
        Optional<Recipe> foundRecipe = recipeRepository.findById(recipe.getRecipeId());

        // Then
        assertThat(foundRecipe).isPresent();
        assertThat(foundRecipe.get().getDescription()).isEqualTo("Test Description");
        assertThat(foundRecipe.get().getNumberOfServings()).isEqualTo(4);
        assertThat(foundRecipe.get().getRecipeType()).isEqualTo("Main Course");
        assertThat(foundRecipe.get().getWeather()).isEqualTo("Winter");
        assertThat(foundRecipe.get().getProducts()).contains(product);
    }

    @Test
    @Transactional
    public void testUpdateRecipe() {
        recipeRepository.save(recipe);
        Recipe savedRecipe = recipeRepository.findById(recipe.getRecipeId()).orElseThrow();

        savedRecipe.setDescription("Updated Description");
        savedRecipe.setNumberOfServings(2);
        savedRecipe.setRecipeType(RecipeType.AMERICAN);
        savedRecipe.setWeather(Weather.SNOWY);
        recipeRepository.saveAndFlush(savedRecipe);

        Recipe updatedRecipe = recipeRepository.findById(savedRecipe.getRecipeId()).orElseThrow();
        assertThat(updatedRecipe).isNotNull();
        assertThat(updatedRecipe.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedRecipe.getNumberOfServings()).isEqualTo(2);
        assertThat(updatedRecipe.getRecipeType()).isEqualTo(RecipeType.AMERICAN);
        assertThat(updatedRecipe.getWeather()).isEqualTo(Weather.SNOWY);
    }

    @Test
    @Transactional
    public void testDeleteRecipe() {
        recipeRepository.save(recipe);
        Recipe savedRecipe = recipeRepository.findById(recipe.getRecipeId()).orElseThrow();

        recipeRepository.delete(savedRecipe);

        Optional<Recipe> foundRecipe = recipeRepository.findById(savedRecipe.getRecipeId());
        assertThat(foundRecipe).isNotPresent();
    }
}
