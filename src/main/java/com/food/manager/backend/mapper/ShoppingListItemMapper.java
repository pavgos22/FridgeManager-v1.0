package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.entity.ShoppingListItem;
import com.food.manager.backend.entity.Comment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
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

    public Map<String, String> toCommentMap(ShoppingListItem item) {
        if (item == null) {
            return null;
        }
        return item.getComments().stream()
                .collect(Collectors.toMap(comment -> comment.getAuthor().getUsername(), Comment::getContent));
    }
}
