package com.food.manager.mapper;

import com.food.manager.dto.response.FridgeResponse;
import com.food.manager.entity.Fridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FridgeMapper {

    public FridgeResponse toFridgeResponse(Fridge fridge) {
        if (fridge == null) {
            return null;
        }
        return new FridgeResponse(
                fridge.getFridgeId(),
                fridge.getGroup().getGroupId(),
                fridge.getProducts()
        );
    }

    public List<FridgeResponse> mapToFridgeList(List<Fridge> fridges) {
        return fridges.stream()
                .map(this::toFridgeResponse)
                .collect(Collectors.toList());
    }
}