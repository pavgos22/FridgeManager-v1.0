package com.food.manager.dto.response;

import com.food.manager.entity.FridgeProduct;

import java.util.List;

public record FridgeResponse(Long fridgeId, Long group, List<FridgeProduct> fridgeProducts) {
}
