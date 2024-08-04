package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.product.CreateProductRequest;
import com.food.manager.backend.dto.request.product.UpdateProductRequest;
import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.dto.response.ProductResponse;
import com.food.manager.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{id}/nutrition")
    public ResponseEntity<NutritionResponse> getProductNutrition(@PathVariable Long id) {
        NutritionResponse nutritionResponse = productService.getProductNutrition(id);
        return ResponseEntity.ok(nutritionResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        ProductResponse productResponse = productService.createProduct(createProductRequest);
        return ResponseEntity.ok(productResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest) {
        if (!id.equals(updateProductRequest.productId())) {
            return ResponseEntity.badRequest().build();
        }
        ProductResponse productResponse = productService.updateProduct(updateProductRequest);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.noContent().build();
    }
}