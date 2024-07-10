package com.food.manager.entity;

import com.food.manager.repository.ShoppingListItemRepository;
import com.food.manager.repository.ProductRepository;
import com.food.manager.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ShoppingListItemTestSuite {

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupRepository groupRepository;

    private ShoppingListItem shoppingListItem;
    private Product product;
    private Group group;

    @BeforeEach
    public void setUp() {
        group = new Group("TestGroup", LocalDateTime.now(), LocalDateTime.now());
        groupRepository.save(group);

        product = new Product("TestProduct");
        productRepository.save(product);

        shoppingListItem = new ShoppingListItem();
        shoppingListItem.setQuantityType("g");
        shoppingListItem.setQuantity(500);
        shoppingListItem.setChecked(false);
        shoppingListItem.setProduct(product);
        shoppingListItem.setGroup(group);
    }

    @Test
    @Transactional
    public void testCreateShoppingListItem() {
        shoppingListItemRepository.save(shoppingListItem);
        Optional<ShoppingListItem> foundItem = shoppingListItemRepository.findById(shoppingListItem.getItemId());

        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getQuantityType()).isEqualTo("g");
        assertThat(foundItem.get().getQuantity()).isEqualTo(500);
        assertThat(foundItem.get().isChecked()).isFalse();
        assertThat(foundItem.get().getProduct().getProductName()).isEqualTo("TestProduct");
        assertThat(foundItem.get().getGroup().getGroupName()).isEqualTo("TestGroup");
    }

    @Test
    @Transactional
    public void testUpdateShoppingListItem() {
        shoppingListItemRepository.save(shoppingListItem);
        ShoppingListItem savedItem = shoppingListItemRepository.findById(shoppingListItem.getItemId()).orElseThrow();

        savedItem.setQuantityType("l");
        savedItem.setQuantity(3);
        savedItem.setChecked(true);
        shoppingListItemRepository.save(savedItem);

        ShoppingListItem updatedItem = shoppingListItemRepository.findById(savedItem.getItemId()).orElseThrow();
        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getQuantityType()).isEqualTo("l");
        assertThat(updatedItem.getQuantity()).isEqualTo(3);
        assertThat(updatedItem.isChecked()).isTrue();
    }

    @Test
    @Transactional
    public void testDeleteShoppingListItem() {
        shoppingListItemRepository.save(shoppingListItem);
        ShoppingListItem savedItem = shoppingListItemRepository.findById(shoppingListItem.getItemId()).orElseThrow();

        shoppingListItemRepository.delete(savedItem);

        Optional<ShoppingListItem> foundItem = shoppingListItemRepository.findById(savedItem.getItemId());
        assertThat(foundItem).isNotPresent();
    }
}