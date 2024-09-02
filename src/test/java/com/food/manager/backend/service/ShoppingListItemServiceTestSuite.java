package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.item.AddItemToListRequest;
import com.food.manager.backend.dto.request.item.RemoveItemFromListRequest;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.entity.Group;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.entity.ShoppingListItem;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.exception.*;
import com.food.manager.backend.repository.GroupRepository;
import com.food.manager.backend.repository.ProductRepository;
import com.food.manager.backend.repository.ShoppingListItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ShoppingListItemServiceTestSuite {

    @Autowired
    private ShoppingListItemService shoppingListItemService;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupRepository groupRepository;

    private Product testProduct;
    private ShoppingListItem testItem;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        testProduct = productRepository.save(new Product("Test Prod " + UUID.randomUUID()));
        testGroup = groupRepository.save(new Group("Test Group", now, now));
        testItem = new ShoppingListItem(testProduct, QuantityType.PIECE, 2, false, testGroup);
        shoppingListItemRepository.save(testItem);
        System.out.println("testItem saved with ID: " + testItem.getItemId());
    }


    @Test
    void getAllItemsSuccessfully() {
        List<ShoppingListItemResponse> items = shoppingListItemService.getAllItems();

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(testProduct.getProductId(), items.getLast().getProductId());
    }

    @Test
    void getItemSuccessfully() {
        ShoppingListItemResponse itemResponse = shoppingListItemService.getItem(testItem.getItemId());

        assertNotNull(itemResponse);
        assertEquals(testItem.getItemId(), itemResponse.getItemId());
        assertEquals(testProduct.getProductId(), itemResponse.getProductId());
    }


    @Test
    void getItemThrowsShoppingListItemNotFoundException() {
        Long invalidItemId = 999L;

        assertThrows(ShoppingListItemNotFoundException.class, () -> {
            shoppingListItemService.getItem(invalidItemId);
        });
    }

    @Test
    void getItemCommentsThrowsShoppingListItemNotFoundException() {
        Long invalidItemId = 999L;

        assertThrows(ShoppingListItemNotFoundException.class, () -> {
            shoppingListItemService.getItemComments(invalidItemId);
        });
    }

    @Test
    void addItemToShoppingListSuccessfully() {
        AddItemToListRequest request = new AddItemToListRequest(testProduct.getProductId(), testGroup.getGroupId(), QuantityType.PIECE, 5);

        ShoppingListItemResponse response = shoppingListItemService.addItemToShoppingList(request);

        assertNotNull(response);
        assertEquals(7, response.getQuantity());
        assertEquals(testProduct.getProductId(), response.getProductId());
        assertEquals(testGroup.getGroupId(), response.getGroupId());

        Optional<ShoppingListItem> savedItem = shoppingListItemRepository.findByProductAndGroup(testProduct, testGroup);
        assertTrue(savedItem.isPresent());
        assertEquals(7, savedItem.get().getQuantity());
    }

    @Test
    void addItemToShoppingListIncreasesQuantityIfItemExists() {
        AddItemToListRequest initialRequest = new AddItemToListRequest(testProduct.getProductId(), testGroup.getGroupId(), QuantityType.PIECE, 5);
        shoppingListItemService.addItemToShoppingList(initialRequest);

        AddItemToListRequest additionalRequest = new AddItemToListRequest(testProduct.getProductId(), testGroup.getGroupId(), QuantityType.PIECE, 3);
        ShoppingListItemResponse response = shoppingListItemService.addItemToShoppingList(additionalRequest);

        assertNotNull(response);
        assertEquals(10, response.getQuantity());
        assertEquals(testProduct.getProductId(), response.getProductId());
        assertEquals(testGroup.getGroupId(), response.getGroupId());

        Optional<ShoppingListItem> savedItem = shoppingListItemRepository.findByProductAndGroup(testProduct, testGroup);
        assertTrue(savedItem.isPresent());
        assertEquals(10, savedItem.get().getQuantity());
    }

    @Test
    void addItemToShoppingListThrowsNegativeValueException() {
        AddItemToListRequest request = new AddItemToListRequest(testProduct.getProductId(), testGroup.getGroupId(), QuantityType.PIECE, -1);

        NegativeValueException exception = assertThrows(NegativeValueException.class, () -> {
            shoppingListItemService.addItemToShoppingList(request);
        });

        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @Test
    void addItemToShoppingListThrowsProductNotFoundInProductsException() {
        AddItemToListRequest request = new AddItemToListRequest(999L, testGroup.getGroupId(), QuantityType.PIECE, 5);

        ProductNotFoundInProductsException exception = assertThrows(ProductNotFoundInProductsException.class, () -> {
            shoppingListItemService.addItemToShoppingList(request);
        });

        assertEquals("Product with ID: " + request.productId() + " not found", exception.getMessage());
    }

    @Test
    void addItemToShoppingListThrowsGroupNotFoundException() {
        AddItemToListRequest request = new AddItemToListRequest(testProduct.getProductId(), 9999999L, QuantityType.PIECE, 5);

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> {
            shoppingListItemService.addItemToShoppingList(request);
        });

        assertEquals("Group with ID: " + request.groupId() + " not found", exception.getMessage());
    }

    @Test
    void removeItemFromShoppingListSuccessfullyRemovesItemWhenQuantityBecomesZero() {
        RemoveItemFromListRequest request = new RemoveItemFromListRequest(testItem.getItemId(), testGroup.getGroupId(), 5);

        shoppingListItemService.removeItemFromShoppingList(request);

        Optional<ShoppingListItem> updatedItem = shoppingListItemRepository.findById(testItem.getItemId());

        assertFalse(updatedItem.isPresent());
    }

    @Test
    void removeItemFromShoppingListThrowsNegativeValueExceptionForInvalidQuantity() {
        RemoveItemFromListRequest request = new RemoveItemFromListRequest(testItem.getItemId(), testGroup.getGroupId(), -1);

        NegativeValueException exception = assertThrows(NegativeValueException.class, () -> {
            shoppingListItemService.removeItemFromShoppingList(request);
        });

        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @Test
    void removeItemFromShoppingListThrowsExceptionWhenItemNotFound() {
        Long invalidItemId = 999L;
        RemoveItemFromListRequest request = new RemoveItemFromListRequest(invalidItemId, testGroup.getGroupId(), 1);

        ShoppingListItemNotFoundException exception = assertThrows(ShoppingListItemNotFoundException.class, () -> {
            shoppingListItemService.removeItemFromShoppingList(request);
        });

        assertEquals("Item with ID: " + request.itemId() + " not found", exception.getMessage());
    }

    // TODO: Fix test
    @Test
    void removeItemFromShoppingListThrowsExceptionWhenItemDoesNotBelongToGroup() {
        Group anotherGroup = groupRepository.save(new Group("Another Group", LocalDateTime.now(), LocalDateTime.now()));
        RemoveItemFromListRequest request = new RemoveItemFromListRequest(testItem.getItemId(), anotherGroup.getGroupId(), 1);

        ShoppingListItemNotInGroupException exception = assertThrows(ShoppingListItemNotInGroupException.class, () -> {
            shoppingListItemService.removeItemFromShoppingList(request);
        });

        assertEquals("Item with ID: " + request.itemId() + " does not belong to group with ID: " + anotherGroup.getGroupId(), exception.getMessage());
    }

    @Test
    void deleteItemSuccessfully() {
        Long itemId = testItem.getItemId();

        shoppingListItemService.deleteItem(itemId);

        Optional<ShoppingListItem> deletedItem = shoppingListItemRepository.findById(itemId);
        assertFalse(deletedItem.isPresent(), "Item should be deleted successfully");
    }

    @Test
    void deleteItemThrowsExceptionWhenItemNotFound() {
        Long nonExistentItemId = 999L;

        ShoppingListItemNotFoundException exception = assertThrows(ShoppingListItemNotFoundException.class, () -> {
            shoppingListItemService.deleteItem(nonExistentItemId);
        });

        assertEquals("Item with ID: " + nonExistentItemId + " not found", exception.getMessage());
    }

}
