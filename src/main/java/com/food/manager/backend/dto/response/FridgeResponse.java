package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.FridgeProduct;

import java.util.List;

public record FridgeResponse(Long fridgeId, Long group, List<FridgeProduct> fridgeProducts) {
}
