package com.food.manager.mapper;

import com.food.manager.dto.response.FridgeProductResponse;
import com.food.manager.entity.FridgeProduct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FridgeProductMapper {

    public FridgeProductResponse toFridgeProductResponse(FridgeProduct fridgeProduct) {
        if (fridgeProduct == null) {
            return null;
        }
        return new FridgeProductResponse(
                fridgeProduct.getFridgeProductId(),
                fridgeProduct.getQuantityType(),
                fridgeProduct.getQuantity(),
                fridgeProduct.getFridge().getFridgeId(),
                fridgeProduct.getProduct()
        );
    }

    public List<FridgeProductResponse> mapToFridgeProductList(List<FridgeProduct> fridgeProducts) {
        return fridgeProducts.stream()
                .map(this::toFridgeProductResponse)
                .collect(Collectors.toList());
    }
}
