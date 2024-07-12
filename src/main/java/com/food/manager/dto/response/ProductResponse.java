package com.food.manager.dto.response;

import com.food.manager.entity.*;

import java.util.List;

public record ProductResponse(Long productId, String productName, ShoppingListItem item, Nutrition nutrition, List<ShoppingListItem> shoppingListItems, List<Recipe> recipes, List<FridgeProduct> fridgeProducts) {
}
