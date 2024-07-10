package com.food.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ShoppingListItemDto {
    private Long itemId;
    private String quantityType;
    private int quantity;
    private boolean checked;
    private Long productId;
    private Long groupId;
    private List<Long> commentIds;
}
