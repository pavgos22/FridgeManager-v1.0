package com.food.manager.service;

import com.food.manager.config.OAuthService;
import com.food.manager.dto.request.fridge.AddProductRequest;
import com.food.manager.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.dto.request.fridgeproduct.AddFridgeProductRequest;
import com.food.manager.dto.response.FridgeProductResponse;
import com.food.manager.dto.response.FridgeResponse;
import com.food.manager.entity.*;
import com.food.manager.enums.QuantityType;
import com.food.manager.exception.FridgeNotFoundException;
import com.food.manager.exception.FridgeProductNotFoundException;
import com.food.manager.mapper.FridgeMapper;
import com.food.manager.mapper.FridgeProductMapper;
import com.food.manager.repository.FridgeProductRepository;
import com.food.manager.repository.FridgeRepository;
import com.food.manager.repository.GroupRepository;
import com.food.manager.repository.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public FridgeProduct addFridgeProduct(QuantityType quantityType, int quantity, Long productId) {
        if (quantity <= 0)
            throw new RuntimeException("Quantity must be greater than zero");


        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        Product product = optionalProduct.get();

        FridgeProduct fridgeProduct = new FridgeProduct(quantityType, quantity, product);
        fridgeProductRepository.save(fridgeProduct);
        return fridgeProduct;
    }

    public FridgeResponse addProductToFridge(AddProductRequest addProductRequest) {
        Fridge fridge = fridgeRepository.findById(addProductRequest.fridgeId())
                .orElseThrow(() -> new FridgeNotFoundException("Fridge with id " + addProductRequest.fridgeId() + " not found"));

        Optional<FridgeProduct> existingProduct = fridge.getProducts().stream()
                .filter(fp -> fp.getProduct().getProductId().equals(addProductRequest.productId())).findFirst();

        FridgeProduct fridgeProduct;
        if (existingProduct.isPresent()) {
            fridgeProduct = existingProduct.get();
            fridgeProduct.setQuantity(fridgeProduct.getQuantity() + addProductRequest.quantity());
        } else {
            Product product = productRepository.findById(addProductRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + addProductRequest.productId()));
            fridgeProduct = new FridgeProduct(addProductRequest.quantityType(), addProductRequest.quantity(), fridge, product);
            fridge.getProducts().add(fridgeProduct);
        }

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
            }
            else
                fridgeProductRepository.save(fridgeProduct);


            fridgeRepository.save(fridge);

            return fridgeMapper.toFridgeResponse(fridge);
        }
        else
            throw new RuntimeException("Fridge not found with id: " + removeProductFromFridgeRequest.fridgeId());
    }

}