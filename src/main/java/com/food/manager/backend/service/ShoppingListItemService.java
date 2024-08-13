package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.item.AddItemToListRequest;
import com.food.manager.backend.dto.request.item.RemoveItemFromListRequest;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.entity.Group;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.entity.ShoppingListItem;
import com.food.manager.backend.exception.GroupNotFoundException;
import com.food.manager.backend.exception.NegativeValueException;
import com.food.manager.backend.exception.ProductNotFoundInProductsException;
import com.food.manager.backend.exception.ShoppingListItemNotFoundException;
import com.food.manager.backend.mapper.ShoppingListItemMapper;
import com.food.manager.backend.repository.GroupRepository;
import com.food.manager.backend.repository.ProductRepository;
import com.food.manager.backend.repository.ShoppingListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingListItemService {

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;
    @Autowired
    private ShoppingListItemMapper shoppingListItemMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GroupRepository groupRepository;

    public List<ShoppingListItemResponse> getAllItems() {
        List<ShoppingListItem> items = shoppingListItemRepository.findAll();
        return shoppingListItemMapper.mapToShoppingListItemList(items);
    }

    public ShoppingListItemResponse getItem(Long id) {
        Optional<ShoppingListItem> itemOptional = shoppingListItemRepository.findById(id);
        if (itemOptional.isPresent()) {
            return shoppingListItemMapper.toShoppingListItemResponse(itemOptional.get());
        } else {
            throw new ShoppingListItemNotFoundException("Item not found with id: " + id);
        }
    }

    public Map<String, String> getItemComments(Long itemId) {
        ShoppingListItem item = shoppingListItemRepository.findById(itemId)
                .orElseThrow(() -> new ShoppingListItemNotFoundException("Item with ID: " + itemId + " not found"));

        return shoppingListItemMapper.toCommentMap(item);
    }

    public ShoppingListItemResponse addItemToShoppingList(AddItemToListRequest addItemToListRequest) {
        if (addItemToListRequest.quantity() <= 0)
            throw new NegativeValueException("Quantity must be greater than zero");

        Product product = productRepository.findById(addItemToListRequest.productId())
                .orElseThrow(() -> new ProductNotFoundInProductsException("Product not found in the products list"));

        Group group = groupRepository.findById(addItemToListRequest.groupId())
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        Optional<ShoppingListItem> existingItem = shoppingListItemRepository.findByProductAndGroup(product, group);

        ShoppingListItem shoppingListItem;

        if (existingItem.isPresent()) {
            shoppingListItem = existingItem.get();
            shoppingListItem.setQuantity(shoppingListItem.getQuantity() + addItemToListRequest.quantity());
        } else {
            shoppingListItem = new ShoppingListItem(product, addItemToListRequest.quantityType(), addItemToListRequest.quantity(), false, group);
            shoppingListItemRepository.save(shoppingListItem);
        }

        return shoppingListItemMapper.toShoppingListItemResponse(shoppingListItem);
    }

    public void removeItemFromShoppingList(RemoveItemFromListRequest removeItemFromListRequest) {
        if(removeItemFromListRequest.quantity() <= 0)
            throw new NegativeValueException("Quantity must be greater than zero");
        ShoppingListItem shoppingListItem = shoppingListItemRepository.findById(removeItemFromListRequest.itemId())
                .orElseThrow(() -> new RuntimeException("Shopping List Item not found"));

        if (!shoppingListItem.getGroup().getGroupId().equals(removeItemFromListRequest.groupId())) {
            throw new RuntimeException("Shopping List Item does not belong to the specified group");
        }

        int newQuantity = shoppingListItem.getQuantity() - removeItemFromListRequest.quantity();
        if (newQuantity > 0) {
            shoppingListItem.setQuantity(newQuantity);
            shoppingListItemRepository.save(shoppingListItem);
        } else {
            shoppingListItemRepository.delete(shoppingListItem);
        }
    }

    public void deleteItem(Long id) {
        if (shoppingListItemRepository.existsById(id)) {
            shoppingListItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("Item not found with id: " + id);
        }
    }

    public void deleteAllItems() {
        shoppingListItemRepository.deleteAll();
    }
}
