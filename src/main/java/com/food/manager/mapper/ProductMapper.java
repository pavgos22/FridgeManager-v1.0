package com.food.manager.mapper;

import com.food.manager.dto.response.ProductResponse;
import com.food.manager.entity.Product;
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
                product.getRecipes()
        );
    }

    public List<ProductResponse> mapToProductList(List<Product> products) {
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }
}
