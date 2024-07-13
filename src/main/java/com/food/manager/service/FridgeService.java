package com.food.manager.service;

import com.food.manager.dto.request.fridge.AddProductToFridgeRequest;
import com.food.manager.dto.request.fridge.CreateFridgeRequest;
import com.food.manager.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.dto.response.FridgeResponse;
import com.food.manager.entity.Fridge;
import com.food.manager.entity.FridgeProduct;
import com.food.manager.mapper.FridgeMapper;
import com.food.manager.repository.FridgeRepository;
import com.food.manager.repository.FridgeProductRepository;
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
    private ProductRepository productRepository;

    @Autowired
    private FridgeProductService fridgeProductService;

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

    public FridgeResponse createFridge(CreateFridgeRequest createFridgeRequest) {
        Optional<Fridge> fridgeOptional = fridgeRepository.findById(createFridgeRequest.groupId());

        if (fridgeOptional.isPresent()) {
            Fridge fridge = fridgeOptional.get();
            return fridgeMapper.toFridgeResponse(fridgeRepository.save(fridge));
        } else {
            throw new RuntimeException("Group not found with id: " + createFridgeRequest.groupId());
        }
    }

    public FridgeResponse addProductToFridge(AddProductToFridgeRequest addProductToFridgeRequest) {

        Optional<Fridge> fridgeOptional = fridgeRepository.findById(addProductToFridgeRequest.fridgeId());
        Optional<FridgeProduct> fridgeProductOptional = fridgeProductRepository.findById(addProductToFridgeRequest.FridgeProductId());

        if (fridgeOptional.isPresent() && fridgeProductOptional.isPresent()) {
            Fridge fridge = fridgeOptional.get();
            FridgeProduct fridgeProduct = fridgeProductOptional.get();

            Optional<FridgeProduct> existingProductOptional = fridge.getProducts().stream()
                    .filter(fp -> fp.getProduct().getProductName().equals(fridgeProduct.getProduct().getProductName()))
                    .findFirst();

            if (existingProductOptional.isPresent()) {
                FridgeProduct existingProduct = existingProductOptional.get();
                existingProduct.setQuantity(existingProduct.getQuantity() + addProductToFridgeRequest.quantity());
                //fridgeProductRepository.save(existingProduct);
                fridgeProductRepository.delete(fridgeProduct);
            } else {
                fridgeProduct.setFridge(fridge);
                fridge.getProducts().add(fridgeProduct);
                //fridgeProductRepository.save(fridgeProduct);
            }

            fridgeRepository.save(fridge);

            return fridgeMapper.toFridgeResponse(fridge);
        } else {
            throw new RuntimeException("Fridge or FridgeProduct not found with given IDs");
        }
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