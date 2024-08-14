package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.product.CreateProductRequest;
import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.dto.response.ProductResponse;
import com.food.manager.backend.entity.Nutrition;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.enums.ProductGroup;
import com.food.manager.backend.exception.NutritionIsNullException;
import com.food.manager.backend.exception.ProductNotFoundInProductsException;
import com.food.manager.backend.mapper.NutritionMapper;
import com.food.manager.backend.mapper.ProductMapper;
import com.food.manager.backend.repository.NutritionRepository;
import com.food.manager.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProductServiceTestSuite {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private NutritionMapper nutritionMapper;

    private Product product;
    private Nutrition nutrition;

    @BeforeEach
    void setUp() {
        nutrition = new Nutrition(100, 10, 5, 20);
        product = new Product("Test Product", ProductGroup.TEST);
        product.setNutrition(nutrition);

        nutritionRepository.save(nutrition);
        productRepository.save(product);
    }

    @Test
    void getProductSuccessfully() {
        ProductResponse response = productService.getProduct(product.getProductId());

        assertNotNull(response);
        assertEquals(product.getProductName(), response.getProductName());
        assertEquals(product.getProductGroup(), response.getProductGroup());
    }

    @Test
    void getProductThrowsRuntimeExceptionWhenNotFound() {
        Long invalidProductId = 999L;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.getProduct(invalidProductId);
        });

        assertEquals("Product not found with id: " + invalidProductId, exception.getMessage());
    }

    @Test
    void getProductNutritionSuccessfully() {
        NutritionResponse response = productService.getProductNutrition(product.getProductId());

        assertNotNull(response);
        assertEquals(nutrition.getCalories(), response.getCalories());
        assertEquals(nutrition.getProtein(), response.getProtein());
        assertEquals(nutrition.getFat(), response.getFat());
        assertEquals(nutrition.getCarbohydrate(), response.getCarbohydrate());
    }

    @Test
    void getProductNutritionThrowsNutritionIsNullExceptionWhenNutritionIsNull() {
        Product productWithoutNutrition = new Product("Test Product Without Nutrition", ProductGroup.TEST);
        productRepository.save(productWithoutNutrition);

        NutritionIsNullException exception = assertThrows(NutritionIsNullException.class, () -> {
            productService.getProductNutrition(productWithoutNutrition.getProductId());
        });

        assertEquals("Nutrition for product with id " + productWithoutNutrition.getProductId() + " is Null", exception.getMessage());
    }

    @Test
    void getAllProductsSuccessfully() {
        Product testProduct = new Product("Unique Test Product", ProductGroup.TEST);
        productRepository.save(testProduct);

        List<ProductResponse> responses = productService.getAllProducts();

        List<ProductResponse> filteredResponses = responses.stream()
                .filter(response -> response.getProductName().equals("Unique Test Product"))
                .toList();

        assertFalse(filteredResponses.isEmpty());
        assertEquals(1, filteredResponses.size());

        ProductResponse response = filteredResponses.getFirst();
        assertEquals(testProduct.getProductName(), response.getProductName());
        assertEquals(testProduct.getProductGroup(), response.getProductGroup());

        productRepository.delete(testProduct);
    }

    @Test
    void createProductSuccessfully() {
        String productName = "Lettuce";
        ProductGroup productGroup = ProductGroup.TEST;
        CreateProductRequest createProductRequest = new CreateProductRequest(productName, productGroup);

        ProductResponse productResponse = productService.createProduct(createProductRequest);

        assertNotNull(productResponse);
        assertEquals(productName, productResponse.getProductName());
        assertEquals(productGroup, productResponse.getProductGroup());

        Product savedProduct = productRepository.findById(productResponse.getProductId()).orElse(null);
        assertNotNull(savedProduct);
        assertEquals(productName, savedProduct.getProductName());
        assertEquals(productGroup, savedProduct.getProductGroup());

        productRepository.delete(savedProduct);
    }

    @Test
    void createProductFromWishlistSuccessfully() {
        String productName = "Mango";

        productService.createProductFromWishlist(productName);

        Product savedProduct = productRepository.findByProductName(productName).orElse(null);
        assertNotNull(savedProduct);
        assertEquals(productName, savedProduct.getProductName());

        productRepository.delete(savedProduct);
    }

    @Test
    void createProductFromWishlistThrowsExceptionWhenProductNotFoundInAPI() {
        String invalidProductName = "NonExistentProduct";

        ProductNotFoundInProductsException exception = assertThrows(ProductNotFoundInProductsException.class, () -> {
            productService.createProductFromWishlist(invalidProductName);
        });

        assertEquals("Product not found in API: " + invalidProductName, exception.getMessage());
    }



}
