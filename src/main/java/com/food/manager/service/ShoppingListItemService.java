package com.food.manager.service;

import com.food.manager.dto.response.ShoppingListItemResponse;
import com.food.manager.entity.ShoppingListItem;
import com.food.manager.mapper.ShoppingListItemMapper;
import com.food.manager.repository.ProductRepository;
import com.food.manager.repository.ShoppingListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListItemService {

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private ShoppingListItemMapper shoppingListItemMapper;

    public List<ShoppingListItemResponse> getAllItems() {
        List<ShoppingListItem> items = shoppingListItemRepository.findAll();
        return shoppingListItemMapper.mapToShoppingListItemList(items);
    }

    public ShoppingListItemResponse getItem(Long id) {
        Optional<ShoppingListItem> itemOptional = shoppingListItemRepository.findById(id);
        if (itemOptional.isPresent()) {
            return shoppingListItemMapper.toShoppingListItemResponse(itemOptional.get());
        } else {
            throw new RuntimeException("Item not found with id: " + id);
        }
    }
}
