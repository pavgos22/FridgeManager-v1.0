package com.food.manager.controller;

import com.food.manager.dto.request.product.CreateNutritionRequest;
import com.food.manager.dto.request.product.CreateProductRequest;
import com.food.manager.dto.request.product.UpdateProductRequest;
import com.food.manager.dto.request.product.AddNutritionRequest;
import com.food.manager.dto.response.ProductResponse;
import com.food.manager.service.ProductService;
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

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateNutritionRequest createNutritionRequest, @RequestBody CreateProductRequest createProductRequest) {
        ProductResponse productResponse = productService.createProduct(createNutritionRequest, createProductRequest);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest) {
        if (!id.equals(updateProductRequest.productId())) {
            return ResponseEntity.badRequest().build();
        }
        ProductResponse productResponse = productService.updateProduct(updateProductRequest);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping("/addNutrition")
    public ResponseEntity<ProductResponse> addNutrition(@RequestBody AddNutritionRequest addNutritionRequest) {
        ProductResponse productResponse = productService.addNutrition(addNutritionRequest);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}