package com.food.manager.service;

import com.food.manager.dto.request.fridgeproduct.AddFridgeProductRequest;
import com.food.manager.dto.response.FridgeProductResponse;
import com.food.manager.entity.FridgeProduct;
import com.food.manager.entity.Product;
import com.food.manager.mapper.FridgeProductMapper;
import com.food.manager.repository.FridgeProductRepository;
import com.food.manager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FridgeProductService {

    @Autowired
    private FridgeProductRepository fridgeProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FridgeProductMapper fridgeProductMapper;

    public FridgeProductResponse addFridgeProduct(AddFridgeProductRequest addFridgeProductRequest) {
        Optional<Product> productOptional = productRepository.findById(addFridgeProductRequest.productId());

        if (productOptional.isPresent()) {
            FridgeProduct fridgeProduct = new FridgeProduct(
                    addFridgeProductRequest.quantityType(),
                    addFridgeProductRequest.quantity(),
                    null,
                    productOptional.get()
            );

            fridgeProductRepository.save(fridgeProduct);
            return fridgeProductMapper.toFridgeProductResponse(fridgeProduct);
        } else {
            throw new RuntimeException("Product not found with id: " + addFridgeProductRequest.productId());
        }
    }

    public FridgeProductResponse getFridgeProduct(Long id) {
        Optional<FridgeProduct> fridgeProductOptional = fridgeProductRepository.findById(id);
        if (fridgeProductOptional.isPresent()) {
            return fridgeProductMapper.toFridgeProductResponse(fridgeProductOptional.get());
        } else {
            throw new RuntimeException("FridgeProduct not found with id: " + id);
        }
    }

    public List<FridgeProductResponse> getAllFridgeProducts() {
        List<FridgeProduct> fridgeProducts = fridgeProductRepository.findAll();
        return fridgeProductMapper.mapToFridgeProductList(fridgeProducts);
    }
}