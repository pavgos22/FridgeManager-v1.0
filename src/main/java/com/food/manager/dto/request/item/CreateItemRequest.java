package com.food.manager.dto.request.item;

import com.food.manager.entity.Group;
import com.food.manager.entity.Product;
import com.food.manager.enums.QuantityType;

public record CreateItemRequest(Product product, QuantityType quantityType, int quantity, boolean checked, Group group) {
}
