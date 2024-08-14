package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.product.CreateProductRequest;
import com.food.manager.backend.dto.request.product.UpdateProductRequest;
import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.dto.response.ProductResponse;
import com.food.manager.backend.enums.ProductGroup;
import com.food.manager.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductResponse productResponse;
    private NutritionResponse nutritionResponse;

    @BeforeEach
    void setUp() {
        nutritionResponse = new NutritionResponse(1L, 500, 20.5f, 10.3f, 60.1f);
        productResponse = new ProductResponse(1L, "Milk", ProductGroup.TEST, nutritionResponse, Arrays.asList(1L, 2L));
    }

    @Test
    void getProductSuccessfully() throws Exception {
        Mockito.when(productService.getProduct(anyLong())).thenReturn(productResponse);

        mockMvc.perform(get("/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.productName").value("Milk"))
                .andExpect(jsonPath("$.productGroup").value("TEST"))
                .andExpect(jsonPath("$.nutrition.nutritionId").value(1L))
                .andExpect(jsonPath("$.recipeIds[0]").value(1L))
                .andExpect(jsonPath("$.recipeIds[1]").value(2L));
    }

    @Test
    void getProductNutritionSuccessfully() throws Exception {
        Mockito.when(productService.getProductNutrition(anyLong())).thenReturn(nutritionResponse);

        mockMvc.perform(get("/v1/products/{id}/nutrition", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nutritionId").value(1L))
                .andExpect(jsonPath("$.calories").value(500))
                .andExpect(jsonPath("$.protein").value(20.5f))
                .andExpect(jsonPath("$.fat").value(10.3f))
                .andExpect(jsonPath("$.carbohydrate").value(60.1f));
    }

    @Test
    void getAllProductsSuccessfully() throws Exception {
        List<ProductResponse> products = Arrays.asList(productResponse);
        Mockito.when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Milk"))
                .andExpect(jsonPath("$[0].productGroup").value("TEST"));
    }

    @Test
    void createProductSuccessfully() throws Exception {
        CreateProductRequest createProductRequest = new CreateProductRequest("Milk", ProductGroup.TEST);
        Mockito.when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Milk\",\"productGroup\":\"TEST\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.productName").value("Milk"))
                .andExpect(jsonPath("$.productGroup").value("TEST"));
    }

    @Test
    void updateProductSuccessfully() throws Exception {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest(1L, "Updated Milk");
        Mockito.when(productService.updateProduct(any(UpdateProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(patch("/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"productName\":\"Updated Milk\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.productName").value("Milk"))
                .andExpect(jsonPath("$.productGroup").value("TEST"));
    }

    @Test
    void updateProductBadRequest() throws Exception {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest(2L, "Updated Milk");

        mockMvc.perform(patch("/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":2,\"productName\":\"Updated Milk\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProductSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllProductsSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
