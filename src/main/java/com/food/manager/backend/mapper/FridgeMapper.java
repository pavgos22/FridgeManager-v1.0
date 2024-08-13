package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.FridgeResponse;
import com.food.manager.backend.entity.Fridge;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FridgeMapper {

    private final ProductMapper productMapper;
    private final FridgeProductMapper fridgeProductMapper;

    public FridgeMapper(ProductMapper productMapper, FridgeProductMapper fridgeProductMapper) {
        this.productMapper = productMapper;
        this.fridgeProductMapper = fridgeProductMapper;
    }

    public FridgeResponse toFridgeResponse(Fridge fridge) {
        if (fridge == null) {
            return null;
        }
        return new FridgeResponse(
                fridge.getFridgeId(),
                fridge.getGroup().getGroupId(),
                fridgeProductMapper.mapToFridgeProductList(fridge.getProducts())
        );
    }

    public List<FridgeResponse> mapToFridgeList(List<Fridge> fridges) {
        return fridges.stream()
                .map(this::toFridgeResponse)
                .collect(Collectors.toList());
    }
}