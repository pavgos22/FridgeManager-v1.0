package com.food.manager.service;

import com.food.manager.dto.request.product.CreateNutritionRequest;
import com.food.manager.dto.request.product.CreateProductRequest;
import com.food.manager.dto.request.product.UpdateProductRequest;
import com.food.manager.dto.request.product.AddNutritionRequest;
import com.food.manager.dto.response.NutritionResponse;
import com.food.manager.dto.response.ProductResponse;
import com.food.manager.entity.Nutrition;
import com.food.manager.entity.Product;
import com.food.manager.mapper.NutritionMapper;
import com.food.manager.mapper.ProductMapper;
import com.food.manager.repository.NutritionRepository;
import com.food.manager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private NutritionMapper nutritionMapper;

    public ProductResponse getProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productMapper.toProductResponse(productOptional.get());
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.mapToProductList(products);
    }

    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        return null;
    }

    public NutritionResponse createNutrition(CreateNutritionRequest createNutritionRequest) {
        Nutrition nutrition = new Nutrition();
        nutrition.setCalories(createNutritionRequest.calories());
        nutrition.setProtein(createNutritionRequest.protein());
        nutrition.setFat(createNutritionRequest.fat());
        nutrition.setCarbohydrate(createNutritionRequest.carbohydrate());
        nutrition = nutritionRepository.save(nutrition);
        return nutritionMapper.toNutritionResponse(nutrition);
    }

    public ProductResponse createProduct(CreateNutritionRequest createNutritionRequest, CreateProductRequest createProductRequest) {
        NutritionResponse nutrition = createNutrition(createNutritionRequest);
        Product product = new Product(createProductRequest.productName());
        product.setNutrition(nutritionMapper.toEntity(nutrition));
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(UpdateProductRequest updateProductRequest) {
        Optional<Product> productOptional = productRepository.findById(updateProductRequest.productId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setProductName(updateProductRequest.productName());
            product = productRepository.save(product);
            return productMapper.toProductResponse(product);
        } else {
            throw new RuntimeException("Product not found with id: " + updateProductRequest.productId());
        }
    }

    public ProductResponse addNutrition(AddNutritionRequest addNutritionRequest) {
        Optional<Product> productOptional = productRepository.findById(addNutritionRequest.productId());
        Optional<Nutrition> nutritionOptional = nutritionRepository.findById(addNutritionRequest.nutritionId());

        if (productOptional.isPresent() && nutritionOptional.isPresent()) {
            Product product = productOptional.get();
            Nutrition nutrition = nutritionOptional.get();
            product.setNutrition(nutrition);
            product = productRepository.save(product);
            return productMapper.toProductResponse(product);
        } else {
            throw new RuntimeException("Product or Nutrition not found with given ids");
        }
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}