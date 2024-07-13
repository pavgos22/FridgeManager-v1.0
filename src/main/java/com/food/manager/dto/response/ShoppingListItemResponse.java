package com.food.manager.dto.response;

import com.food.manager.entity.Comment;
import com.food.manager.entity.Group;
import com.food.manager.enums.QuantityType;

import java.util.List;

public record ShoppingListItemResponse(Long itemId, QuantityType quantityType, int quantity, boolean checked) {
}
