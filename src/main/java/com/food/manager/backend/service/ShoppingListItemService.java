package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.item.AddItemToListRequest;
import com.food.manager.backend.dto.request.item.RemoveItemFromListRequest;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.entity.Group;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.entity.ShoppingListItem;
import com.food.manager.backend.exception.*;
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
            throw new ShoppingListItemNotFoundException(id);
        }
    }

    public Map<String, String> getItemComments(Long itemId) {
        ShoppingListItem item = shoppingListItemRepository.findById(itemId)
                .orElseThrow(() -> new ShoppingListItemNotFoundException(itemId));

        return shoppingListItemMapper.toCommentMap(item);
    }

    public ShoppingListItemResponse addItemToShoppingList(AddItemToListRequest addItemToListRequest) {
        if (addItemToListRequest.quantity() <= 0)
            throw new NegativeValueException();

        Product product = productRepository.findById(addItemToListRequest.productId())
                .orElseThrow(() -> new ProductNotFoundInProductsException(addItemToListRequest.productId()));

        Group group = groupRepository.findById(addItemToListRequest.groupId())
                .orElseThrow(() -> new GroupNotFoundException(addItemToListRequest.groupId()));

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
            throw new NegativeValueException();
        ShoppingListItem shoppingListItem = shoppingListItemRepository.findById(removeItemFromListRequest.itemId())
                .orElseThrow(() -> new ShoppingListItemNotFoundException(removeItemFromListRequest.itemId()));

        if (!shoppingListItem.getGroup().getGroupId().equals(removeItemFromListRequest.groupId())) {
            throw new ShoppingListItemNotInGroupException(removeItemFromListRequest.itemId(), shoppingListItem.getGroup().getGroupId());
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
            throw new ShoppingListItemNotFoundException(id);
        }
    }

    public void deleteAllItems() {
        shoppingListItemRepository.deleteAll();
    }
}
