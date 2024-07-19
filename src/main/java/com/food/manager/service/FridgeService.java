package com.food.manager.service;

import com.food.manager.dto.request.fridge.AddProductRequest;
import com.food.manager.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.dto.response.FridgeResponse;
import com.food.manager.dto.response.RecipeResponse;
import com.food.manager.entity.*;
import com.food.manager.exception.*;
import com.food.manager.mapper.FridgeMapper;
import com.food.manager.mapper.RecipeMapper;
import com.food.manager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FridgeService {

    @Autowired
    private FridgeRepository fridgeRepository;

    @Autowired
    private FridgeProductRepository fridgeProductRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FridgeMapper fridgeMapper;

    @Autowired
    private RecipeMapper recipeMapper;

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

    public List<RecipeResponse> getRecipesPossibleWithFridgeProducts(Long fridgeId) {
        Fridge fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new RuntimeException("Fridge not found with id: " + fridgeId));

        Set<Product> fridgeProducts = fridge.getProducts().stream()
                .map(FridgeProduct::getProduct)
                .collect(Collectors.toSet());

        List<Recipe> recipes = recipeRepository.findAll();

        List<Recipe> possibleRecipes = recipes.stream()
                .filter(recipe -> !recipe.getIngredients().isEmpty())
                .filter(recipe -> recipe.getIngredients().stream()
                        .allMatch(ingredient -> fridgeProducts.contains(ingredient.getProduct())))
                .collect(Collectors.toList());

        return recipeMapper.mapToRecipeResponseList(possibleRecipes);
    }

    public FridgeResponse executeRecipe(Long fridgeId, Long recipeId) {
        Fridge fridge = fridgeRepository.findById(fridgeId)
                .orElseThrow(() -> new FridgeNotFoundException("Fridge not found with id: " + fridgeId));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));

        Set<FridgeProduct> fridgeProducts = new HashSet<>(fridge.getProducts());

        for (Ingredient ingredient : recipe.getIngredients()) {
            FridgeProduct fridgeProduct = fridgeProducts.stream()
                    .filter(fp -> fp.getProduct().equals(ingredient.getProduct()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundInFridgeException("Product " + ingredient.getProduct().getProductName() + " not found in fridge"));

            if (fridgeProduct.getQuantityType() != ingredient.getQuantityType()) {
                throw new MismatchedQuantityTypeException("Quantity type mismatch for product " + ingredient.getProduct().getProductName());
            }

            if (fridgeProduct.getQuantity() < ingredient.getQuantity()) {
                throw new InsufficientQuantityException("Insufficient quantity for product " + ingredient.getProduct().getProductName());
            }
        }

        for (Ingredient ingredient : recipe.getIngredients()) {
            FridgeProduct fridgeProduct = fridgeProducts.stream()
                    .filter(fp -> fp.getProduct().equals(ingredient.getProduct()))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundInFridgeException("Product " + ingredient.getProduct().getProductName() + " not found in fridge"));

            fridgeProduct.setQuantity(fridgeProduct.getQuantity() - ingredient.getQuantity());

            if (fridgeProduct.getQuantity() == 0) {
                fridge.getProducts().remove(fridgeProduct);
                fridgeProductRepository.delete(fridgeProduct);
            } else {
                fridgeProductRepository.save(fridgeProduct);
            }
        }

        fridgeRepository.save(fridge);
        return fridgeMapper.toFridgeResponse(fridge);
    }
}