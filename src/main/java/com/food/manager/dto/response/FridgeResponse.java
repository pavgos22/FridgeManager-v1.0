package com.food.manager.dto.response;

import com.food.manager.entity.FridgeProduct;
import com.food.manager.entity.Group;

import java.util.List;

public record FridgeResponse(Long fridgeId, Group group, List<FridgeProduct> fridgeProducts) {
}
