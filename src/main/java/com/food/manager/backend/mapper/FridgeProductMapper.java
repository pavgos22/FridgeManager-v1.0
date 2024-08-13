package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.FridgeProductResponse;
import com.food.manager.backend.entity.FridgeProduct;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FridgeProductMapper {

    public FridgeProductResponse toFridgeProductResponse(FridgeProduct fridgeProduct) {
        if (fridgeProduct == null) {
            return null;
        }
        return new FridgeProductResponse(
                fridgeProduct.getFridgeProductId(),
                fridgeProduct.getQuantityType(),
                fridgeProduct.getQuantity(),
                fridgeProduct.getProduct().getProductName()
        );
    }

    public List<FridgeProductResponse> mapToFridgeProductList(List<FridgeProduct> fridgeProducts) {
        return fridgeProducts.stream()
                .map(this::toFridgeProductResponse)
                .collect(Collectors.toList());
    }
}
