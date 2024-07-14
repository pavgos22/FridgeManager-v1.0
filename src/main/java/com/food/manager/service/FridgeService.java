package com.food.manager.service;

import com.food.manager.dto.request.fridge.AddProductRequest;
import com.food.manager.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.dto.request.fridgeproduct.AddFridgeProductRequest;
import com.food.manager.dto.request.product.CreateNutritionRequest;
import com.food.manager.dto.response.FridgeResponse;
import com.food.manager.dto.response.ProductResponse;
import com.food.manager.entity.*;
import com.food.manager.enums.QuantityType;
import com.food.manager.mapper.FridgeMapper;
import com.food.manager.repository.FridgeProductRepository;
import com.food.manager.repository.FridgeRepository;
import com.food.manager.repository.GroupRepository;
import com.food.manager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FridgeService {

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private FridgeProductRepository fridgeProductRepository;

    @Autowired
    private FridgeMapper fridgeMapper;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ProductRepository productRepository;

    public FridgeResponse getFridge(Long id) {
        Optional<Fridge> fridgeOptional = fridgeRepository.findById(id);
        if (fridgeOptional.isPresent()) {
            return fridgeMapper.toFridgeResponse(fridgeOptional.get());
        } else {
            throw new RuntimeException("Fridge not found with id: " + id);
        }
    }

    public List<FridgeResponse> getAllFridges() {
        List<Fridge> fridges = fridgeRepository.findAll();
        return fridgeMapper.mapToFridgeList(fridges);
    }

    public Fridge createFridge(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isEmpty()) {
            throw new RuntimeException("Group not found with id: " + groupId);
        }
        Fridge fridge = new Fridge(optionalGroup.get());
        fridge = fridgeRepository.save(fridge);
        return fridge;
    }

    public Product createProduct(String name) {
        return new Product(name);
    }

    public Nutrition createNutrition(CreateNutritionRequest createNutritionRequest) {
        return new Nutrition(
                createNutritionRequest.calories(),
                createNutritionRequest.protein(),
                createNutritionRequest.fat(),
                createNutritionRequest.carbohydrate()
        );
    }

    public FridgeProduct createFridgeProduct(QuantityType quantityType, int quantity, Long fridgeId, Long productId) {
        Fridge fridge = fridgeRepository.findById(fridgeId).get();
        Product product = productRepository.findById(productId).get();
        return new FridgeProduct(
                quantityType,
                quantity,
                fridge,
                product
        );
    }

    public FridgeResponse addProductToFridge(AddProductRequest addProductRequest) {
        Product product = productRepository.findById(addProductRequest.productId()).get();
        createProduct(product.getProductName());
        productRepository.save(product);
        FridgeProduct fridgeProduct = createFridgeProduct(
                addProductRequest.quantityType(),
                addProductRequest.quantity(),
                addProductRequest.fridgeId(),
                addProductRequest.productId()
        );
        fridgeProductRepository.save(fridgeProduct);

        Fridge fridge = fridgeRepository.findById(addProductRequest.fridgeId()).get();
        fridge.getProducts().add(fridgeProduct);
        fridgeRepository.save(fridge);

        return fridgeMapper.toFridgeResponse(fridge);
    }

    public FridgeResponse removeProductFromFridge(RemoveProductFromFridgeRequest removeProductFromFridgeRequest) {
        Optional<Fridge> fridgeOptional = fridgeRepository.findById(removeProductFromFridgeRequest.fridgeId());

        if (fridgeOptional.isPresent()) {
            Fridge fridge = fridgeOptional.get();

            FridgeProduct fridgeProduct = fridge.getProducts().stream()
                    .filter(fp -> fp.getFridgeProductId().equals(removeProductFromFridgeRequest.fridgeProductId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found in fridge"));

            if (fridgeProduct.getQuantity() < removeProductFromFridgeRequest.quantity()) {
                throw new RuntimeException("Not enough quantity to remove");
            }

            fridgeProduct.setQuantity(fridgeProduct.getQuantity() - removeProductFromFridgeRequest.quantity());

            if (fridgeProduct.getQuantity() == 0) {
                fridge.getProducts().remove(fridgeProduct);
                fridgeProductRepository.delete(fridgeProduct);
            } else {
                fridgeProductRepository.save(fridgeProduct);
            }

            fridgeRepository.save(fridge);

            return fridgeMapper.toFridgeResponse(fridge);
        } else {
            throw new RuntimeException("Fridge not found with id: " + removeProductFromFridgeRequest.fridgeId());
        }
    }
}