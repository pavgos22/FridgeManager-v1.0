package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.entity.ShoppingListItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingListItemMapper {

    public ShoppingListItemResponse toShoppingListItemResponse(ShoppingListItem item) {
        if (item == null) {
            return null;
        }
        return new ShoppingListItemResponse(
                item.getItemId(),
                item.getProduct().getProductId(),
                item.getGroup().getGroupId(),
                item.getQuantityType(),
                item.getQuantity(),
                item.isChecked()
        );
    }

    public List<ShoppingListItemResponse> mapToShoppingListItemList(List<ShoppingListItem> items) {
        return items.stream()
                .map(this::toShoppingListItemResponse)
                .collect(Collectors.toList());
    }
}
