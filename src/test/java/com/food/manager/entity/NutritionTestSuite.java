package com.food.manager.entity;

import com.food.manager.repository.NutritionRepository;
import com.food.manager.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NutritionTestSuite {

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private ProductRepository productRepository;

    private Nutrition nutrition;
    private Product product;

    @BeforeEach
    public void setUp() {
        nutrition = new Nutrition();
        nutrition.setCalories(200);
        nutrition.setProtein(10.0f);
        nutrition.setFat(5.0f);
        nutrition.setCarbohydrate(30.0f);

        product = new Product("TestProduct");
    }

    @Test
    @Transactional
    public void testCreateNutrition() {
        nutritionRepository.save(nutrition);
        Optional<Nutrition> foundNutrition = nutritionRepository.findById(nutrition.getNutritionId());

        assertThat(foundNutrition).isPresent();
        assertThat(foundNutrition.get().getCalories()).isEqualTo(200);
        assertThat(foundNutrition.get().getProtein()).isEqualTo(10.0f);
        assertThat(foundNutrition.get().getFat()).isEqualTo(5.0f);
        assertThat(foundNutrition.get().getCarbohydrate()).isEqualTo(30.0f);
    }

    @Test
    @Transactional
    public void testUpdateNutrition() {
        nutritionRepository.save(nutrition);
        Nutrition savedNutrition = nutritionRepository.findById(nutrition.getNutritionId()).orElseThrow();

        savedNutrition.setCalories(300);
        savedNutrition.setProtein(15.0f);
        savedNutrition.setFat(7.0f);
        savedNutrition.setCarbohydrate(40.0f);
        nutritionRepository.save(savedNutrition);

        Nutrition updatedNutrition = nutritionRepository.findById(savedNutrition.getNutritionId()).orElseThrow();
        assertThat(updatedNutrition).isNotNull();
        assertThat(updatedNutrition.getCalories()).isEqualTo(300);
        assertThat(updatedNutrition.getProtein()).isEqualTo(15.0f);
        assertThat(updatedNutrition.getFat()).isEqualTo(7.0f);
        assertThat(updatedNutrition.getCarbohydrate()).isEqualTo(40.0f);
    }

    @Test
    @Transactional
    public void testDeleteNutrition() {
        nutritionRepository.save(nutrition);
        Nutrition savedNutrition = nutritionRepository.findById(nutrition.getNutritionId()).orElseThrow();

        nutritionRepository.delete(savedNutrition);

        Optional<Nutrition> foundNutrition = nutritionRepository.findById(savedNutrition.getNutritionId());
        assertThat(foundNutrition).isNotPresent();
    }

    @Test
    @Transactional
    public void testAddNutritionToProduct() {
        product.setNutrition(nutrition);
        nutrition.setProduct(product);

        productRepository.save(product);
        nutritionRepository.save(nutrition);

        Product savedProduct = productRepository.findById(product.getProductId()).orElseThrow();
        Nutrition savedNutrition = nutritionRepository.findById(nutrition.getNutritionId()).orElseThrow();

        assertThat(savedProduct.getNutrition()).isEqualTo(nutrition);
        assertThat(savedNutrition.getProduct()).isEqualTo(product);
    }
}
