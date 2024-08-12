package com.food.manager.backend.service;

import com.food.manager.backend.dto.response.FridgeProductResponse;
import com.food.manager.backend.entity.FridgeProduct;
import com.food.manager.backend.mapper.FridgeProductMapper;
import com.food.manager.backend.repository.FridgeProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FridgeProductService {

    @Autowired
    private FridgeProductRepository fridgeProductRepository;

    @Autowired
    private FridgeProductMapper fridgeProductMapper;

    public FridgeProductResponse getFridgeProduct(Long id) {
        Optional<FridgeProduct> fridgeProductOptional = fridgeProductRepository.findById(id);
        if (fridgeProductOptional.isPresent()) {
            FridgeProduct fridgeProduct = fridgeProductOptional.get();
            if (fridgeProduct.getProduct() == null) {
                throw new RuntimeException("FridgeProduct has a null Product with id: " + id);
            }
            return fridgeProductMapper.toFridgeProductResponse(fridgeProduct);
        } else {
            throw new RuntimeException("FridgeProduct not found with id: " + id);
        }
    }


    public List<FridgeProductResponse> getAllFridgeProducts() {
        List<FridgeProduct> fridgeProducts = fridgeProductRepository.findAll();
        return fridgeProductMapper.mapToFridgeProductList(fridgeProducts);
    }
}