package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.ProductResponse;
import com.food.manager.backend.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductMapper {

    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getNutrition(),
                product.getRecipeIds()
        );
    }

    public List<ProductResponse> mapToProductList(List<Product> products) {
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }
}
