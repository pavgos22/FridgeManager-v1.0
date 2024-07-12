package com.food.manager.entity;

import com.food.manager.enums.QuantityType;
import com.food.manager.repository.FridgeProductRepository;
import com.food.manager.repository.FridgeRepository;
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
public class FridgeProductTestSuite {

    @Autowired
    private FridgeProductRepository fridgeProductRepository;

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupRepository groupRepository;

    private FridgeProduct fridgeProduct;
    private Fridge fridge;
    private Product product;
    private Group group;

    @BeforeEach
    public void setUp() {
        group = new Group("TestGroup", LocalDateTime.now(), LocalDateTime.now());
        groupRepository.save(group);

        fridge = new Fridge();
        fridge.setGroup(group);
        fridgeRepository.save(fridge);

        product = new Product("TestProduct");
        productRepository.save(product);

        fridgeProduct = new FridgeProduct(QuantityType.GRAM, 50, fridge, product);
    }

    @Test
    @Transactional
    public void testCreateFridgeProduct() {
        fridgeProductRepository.save(fridgeProduct);
        Optional<FridgeProduct> foundFridgeProduct = fridgeProductRepository.findById(fridgeProduct.getFridgeProductId());

        assertThat(foundFridgeProduct).isPresent();
        assertThat(foundFridgeProduct.get().getQuantityType()).isEqualTo(QuantityType.GRAM);
        assertThat(foundFridgeProduct.get().getQuantity()).isEqualTo(50);
        assertThat(foundFridgeProduct.get().getFridge().getGroup().getGroupName()).isEqualTo("TestGroup");
        assertThat(foundFridgeProduct.get().getProduct().getProductName()).isEqualTo("TestProduct");
    }

    @Test
    @Transactional
    public void testUpdateFridgeProduct() {
        fridgeProductRepository.save(fridgeProduct);
        FridgeProduct savedFridgeProduct = fridgeProductRepository.findById(fridgeProduct.getFridgeProductId()).orElseThrow();

        savedFridgeProduct.setQuantityType(QuantityType.MILLILITER);
        savedFridgeProduct.setQuantity(3000);
        fridgeProductRepository.save(savedFridgeProduct);

        FridgeProduct updatedFridgeProduct = fridgeProductRepository.findById(savedFridgeProduct.getFridgeProductId()).orElseThrow();
        assertThat(updatedFridgeProduct).isNotNull();
        assertThat(updatedFridgeProduct.getQuantityType()).isEqualTo(QuantityType.MILLILITER);
        assertThat(updatedFridgeProduct.getQuantity()).isEqualTo(3000);
    }

    @Test
    @Transactional
    public void testDeleteFridgeProduct() {
        fridgeProductRepository.save(fridgeProduct);
        FridgeProduct savedFridgeProduct = fridgeProductRepository.findById(fridgeProduct.getFridgeProductId()).orElseThrow();

        fridgeProductRepository.delete(savedFridgeProduct);

        Optional<FridgeProduct> foundFridgeProduct = fridgeProductRepository.findById(savedFridgeProduct.getFridgeProductId());
        assertThat(foundFridgeProduct).isNotPresent();
    }
}
