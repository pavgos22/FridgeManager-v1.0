package com.food.manager.entity;

import com.food.manager.repository.ProductRepository;
import com.food.manager.repository.NutritionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductTestSuite {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    private Product product;
    private Nutrition nutrition;

    @BeforeEach
    public void setUp() {
        nutrition = new Nutrition();
        nutrition.setCalories(250);
        nutrition.setProtein(20);
        nutrition.setFat(10);
        nutrition.setCarbohydrate(30);
        nutritionRepository.save(nutrition);

        product = new Product();
        product.setProductName("TestProduct");
        product.setNutrition(nutrition);
    }

    @Test
    @Transactional
    public void testCreateProduct() {
        productRepository.save(product);
        Optional<Product> foundProduct = productRepository.findById(product.getProductId());

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getProductName()).isEqualTo("TestProduct");
        assertThat(foundProduct.get().getNutrition().getCalories()).isEqualTo(250);
    }

    @Test
    @Transactional
    public void testUpdateProduct() {
        productRepository.save(product);
        Product savedProduct = productRepository.findById(product.getProductId()).orElseThrow();

        savedProduct.setProductName("UpdatedProductName");
        productRepository.save(savedProduct);

        Product updatedProduct = productRepository.findById(savedProduct.getProductId()).orElseThrow();
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getProductName()).isEqualTo("UpdatedProductName");
    }

    @Test
    @Transactional
    public void testDeleteProduct() {
        productRepository.save(product);
        Product savedProduct = productRepository.findById(product.getProductId()).orElseThrow();

        productRepository.delete(savedProduct);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getProductId());
        assertThat(foundProduct).isNotPresent();
    }
}
