package com.food.manager.service;

import com.food.manager.config.OAuthService;
import com.food.manager.dto.request.fridge.AddProductRequest;
import com.food.manager.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.dto.request.fridgeproduct.AddFridgeProductRequest;
import com.food.manager.dto.request.product.CreateNutritionRequest;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
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

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private FridgeProductMapper fridgeProductMapper;

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

    public Nutrition createNutrition(CreateNutritionRequest createNutritionRequest) {
        return new Nutrition(
                createNutritionRequest.calories(),
                createNutritionRequest.protein(),
                createNutritionRequest.fat(),
                createNutritionRequest.carbohydrate()
        );
    }

    public Product createProduct(String name) {
        return new Product(name);
    }

    public FridgeProduct createFridgeProduct(QuantityType quantityType, int quantity, Fridge fridge, Product product) {
        return new FridgeProduct(
                quantityType,
                quantity,
                fridge,
                product
        );
    }

    public FridgeProductResponse addFridgeProduct(AddFridgeProductRequest addFridgeProductRequest) {
        Optional<Product> optionalProduct = productRepository.findByProductName(addFridgeProductRequest.productName());
        Product product;

        if (optionalProduct.isEmpty()) {
            product = fetchProductFromAPI(addFridgeProductRequest.productName());
            if (product == null) {
                throw new RuntimeException("Product not found in external API: " + addFridgeProductRequest.productName());
            }
            productRepository.save(product);
        } else {
            product = optionalProduct.get();
        }

        Optional<Fridge> fridgeOptional = fridgeRepository.findById(addFridgeProductRequest.fridgeId());


        if (fridgeOptional.isPresent()) {
            Fridge fridge = fridgeOptional.get();
            FridgeProduct fridgeProduct = new FridgeProduct(
                    addFridgeProductRequest.quantityType(),
                    addFridgeProductRequest.quantity(),
                    null,
                    product
            );
            fridgeProductRepository.save(fridgeProduct);
            return fridgeProductMapper.toFridgeProductResponse(fridgeProduct);
        }
        else
            throw new FridgeNotFoundException("Fridge with ID " + addFridgeProductRequest.fridgeId() + " not found");
    }

    public FridgeResponse addProductToFridge(AddProductRequest addProductRequest) {
        Fridge fridge = fridgeRepository.findById(addProductRequest.fridgeId()).orElseThrow(() -> new FridgeNotFoundException("Fridge with ID: " + addProductRequest.fridgeId()));
        FridgeProduct fridgeProduct = fridgeProductRepository.findById(addProductRequest.fridgeProductId()).orElseThrow(() -> new FridgeProductNotFoundException("FridgeProduct with ID: " + addProductRequest.fridgeProductId()));

        Optional<FridgeProduct> optionalFridgeProduct = fridge.getProducts().stream()
                .filter(fp -> fp.getProduct().equals(fridgeProduct.getProduct()))
                .findFirst();

        if (optionalFridgeProduct.isPresent()) {
            FridgeProduct existingProduct = optionalFridgeProduct.get();
            existingProduct.setQuantity(existingProduct.getQuantity() + fridgeProduct.getQuantity());
            fridgeProductRepository.save(existingProduct);
        } else {
            fridge.getProducts().add(new FridgeProduct(
                    fridgeProduct.getQuantityType(),
                    fridgeProduct.getQuantity(),
                    fridge,
                    fridgeProduct.getProduct()
            ));
        }

        fridgeRepository.save(fridge);

        return fridgeMapper.toFridgeResponse(fridge);
    }



    private Product fetchProductFromAPI(String productName) {
        String token = oAuthService.getOAuthToken();

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://platform.fatsecret.com/rest/server.api?method=foods.search&search_expression=" + productName + "&format=json&page_number=0&max_results=10";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray foods = jsonResponse.getJSONObject("foods").getJSONArray("food");

        for (int i = 0; i < foods.length(); i++) {
            JSONObject food = foods.getJSONObject(i);
            if (food.getString("food_type").equals("Generic")) {
                return new Product(food.getString("food_name"));
            }
        }

        return null;
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