package com.food.manager.entity;

import com.food.manager.enums.QuantityType;
import com.food.manager.repository.FridgeProductRepository;
import com.food.manager.repository.FridgeRepository;
import com.food.manager.repository.GroupRepository;
import com.food.manager.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FridgeTestSuite {

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private FridgeProductRepository fridgeProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupRepository groupRepository;

    private Fridge fridge;

    @Test
    @Transactional
    public void addProductToFridgeTest() {
        Product product = new Product("Butter");
        Product savedProduct = productRepository.save(product);

        Group group = new Group("test group", LocalDateTime.now(), LocalDateTime.now());
        groupRepository.save(group);

        fridge = new Fridge();
        fridge.setGroup(group);
        Fridge savedFridge = fridgeRepository.save(fridge);

        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.GRAM, 1, fridge, product);

        savedFridge.getProducts().add(fridgeProduct);
        fridgeProductRepository.save(fridgeProduct);

        Fridge retrievedFridge = fridgeRepository.findById(savedFridge.getFridgeId()).orElse(null);
        assertThat(retrievedFridge).isNotNull();
        assertThat(retrievedFridge.getProducts().size()).isEqualTo(1);

        FridgeProduct retrievedFridgeProduct = retrievedFridge.getProducts().get(0);
        assertThat(retrievedFridgeProduct.getProduct()).isEqualTo(savedProduct);
        assertThat(retrievedFridgeProduct.getQuantityType()).isEqualTo("g");
        assertThat(retrievedFridgeProduct.getQuantity()).isEqualTo(1);
    }
}