package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.fridge.AddProductRequest;
import com.food.manager.backend.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.backend.dto.response.FridgeResponse;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.entity.*;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import com.food.manager.backend.exception.FridgeNotFoundException;
import com.food.manager.backend.exception.GroupNotFoundException;
import com.food.manager.backend.exception.InsufficientQuantityException;
import com.food.manager.backend.exception.ProductNotFoundInFridgeException;
import com.food.manager.backend.mapper.FridgeMapper;
import com.food.manager.backend.mapper.RecipeMapper;
import com.food.manager.backend.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
public class FridgeServiceTestSuite {

    @Mock
    private FridgeRepository fridgeRepository;

    @Mock
    private FridgeMapper fridgeMapper;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FridgeProductRepository fridgeProductRepository;

    @InjectMocks
    private FridgeService fridgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFridgeWhenFridgeExists() {
        Fridge fridge = new Fridge();
        FridgeResponse fridgeResponse = new FridgeResponse();

        when(fridgeRepository.findById(1L)).thenReturn(Optional.of(fridge));
        when(fridgeMapper.toFridgeResponse(fridge)).thenReturn(fridgeResponse);

        FridgeResponse result = fridgeService.getFridge(1L);

        assertNotNull(result);
        assertEquals(fridgeResponse, result);
        verify(fridgeRepository, times(1)).findById(1L);
        verify(fridgeMapper, times(1)).toFridgeResponse(fridge);
    }

    @Test
    void testGetFridgeWhenFridgeDoesNotExist() {
        when(fridgeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FridgeNotFoundException.class, () -> fridgeService.getFridge(1L));
        verify(fridgeRepository, times(1)).findById(1L);
        verify(fridgeMapper, times(0)).toFridgeResponse(any(Fridge.class));
    }

    @Test
    void testGetAllFridges() {
        Fridge fridge1 = new Fridge();
        Fridge fridge2 = new Fridge();
        List<Fridge> fridges = Arrays.asList(fridge1, fridge2);
        FridgeResponse response1 = new FridgeResponse();
        FridgeResponse response2 = new FridgeResponse();
        List<FridgeResponse> fridgeResponses = Arrays.asList(response1, response2);

        when(fridgeRepository.findAll()).thenReturn(fridges);
        when(fridgeMapper.mapToFridgeList(fridges)).thenReturn(fridgeResponses);

        List<FridgeResponse> result = fridgeService.getAllFridges();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(fridgeResponses, result);
        verify(fridgeRepository, times(1)).findAll();
        verify(fridgeMapper, times(1)).mapToFridgeList(fridges);
    }

    @Test
    void testCreateFridgeWhenGroupExists() {
        Group group = new Group("Test Group");
        Fridge fridge = new Fridge(group);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(fridgeRepository.save(any(Fridge.class))).thenReturn(fridge);

        Fridge result = fridgeService.createFridge(1L);

        assertNotNull(result);
        assertEquals(group, result.getGroup());
        verify(groupRepository, times(1)).findById(1L);
        verify(fridgeRepository, times(1)).save(any(Fridge.class));
    }

    @Test
    void testCreateFridgeWhenGroupDoesNotExist() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> fridgeService.createFridge(1L));
        verify(groupRepository, times(1)).findById(1L);
        verify(fridgeRepository, times(0)).save(any(Fridge.class));
    }

    @Test
    void testAddProductToFridgeWhenFridgeExistsAndProductExistsAddNewProduct() {
        Long fridgeId = 1L;
        Long productId = 1L;
        AddProductRequest request = new AddProductRequest(productId, QuantityType.PIECE, 5);
        Fridge fridge = new Fridge();
        Product product = new Product("Apple");
        product.setProductId(productId);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        FridgeResponse fridgeResponse = new FridgeResponse();
        when(fridgeMapper.toFridgeResponse(fridge)).thenReturn(fridgeResponse);

        FridgeResponse result = fridgeService.addProductToFridge(fridgeId, request);

        assertNotNull(result);
        assertEquals(fridgeResponse, result);
        assertEquals(1, fridge.getProducts().size());
        FridgeProduct fridgeProduct = fridge.getProducts().getFirst();
        assertEquals(product, fridgeProduct.getProduct());
        assertEquals(5, fridgeProduct.getQuantity());
        assertEquals(QuantityType.PIECE, fridgeProduct.getQuantityType());

        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(productRepository, times(1)).findById(productId);
        verify(fridgeRepository, times(1)).save(fridge);
        verify(fridgeMapper, times(1)).toFridgeResponse(fridge);
    }

    @Test
    void testAddProductToFridgeWhenFridgeExistsAndProductAlreadyInFridgeUpdateQuantity() {
        Long fridgeId = 1L;
        Long productId = 1L;
        AddProductRequest request = new AddProductRequest(productId, QuantityType.PIECE, 5);
        Product product = new Product("Apple");
        product.setProductId(productId);
        Fridge fridge = new Fridge();
        FridgeProduct existingProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);
        fridge.getProducts().add(existingProduct);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        FridgeResponse fridgeResponse = new FridgeResponse();
        when(fridgeMapper.toFridgeResponse(fridge)).thenReturn(fridgeResponse);

        FridgeResponse result = fridgeService.addProductToFridge(fridgeId, request);

        assertNotNull(result);
        assertEquals(fridgeResponse, result);
        assertEquals(1, fridge.getProducts().size());
        FridgeProduct fridgeProduct = fridge.getProducts().getFirst();
        assertEquals(product, fridgeProduct.getProduct());
        assertEquals(15, fridgeProduct.getQuantity());
        assertEquals(QuantityType.PIECE, fridgeProduct.getQuantityType());

        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(productRepository, times(0)).findById(anyLong());
        verify(fridgeRepository, times(1)).save(fridge);
        verify(fridgeMapper, times(1)).toFridgeResponse(fridge);
    }

    @Test
    void testAddProductToFridgeWhenFridgeDoesNotExist() {
        Long fridgeId = 1L;
        Long productId = 1L;
        AddProductRequest request = new AddProductRequest(productId, QuantityType.PIECE, 5);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.empty());

        assertThrows(FridgeNotFoundException.class, () -> fridgeService.addProductToFridge(fridgeId, request));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(productRepository, times(0)).findById(anyLong());
        verify(fridgeRepository, times(0)).save(any(Fridge.class));
        verify(fridgeMapper, times(0)).toFridgeResponse(any(Fridge.class));
    }

    @Test
    void testAddProductToFridgeWhenProductDoesNotExist() {
        Long fridgeId = 1L;
        Long productId = 1L;
        AddProductRequest request = new AddProductRequest(productId, QuantityType.PIECE, 5);
        Fridge fridge = new Fridge();

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fridgeService.addProductToFridge(fridgeId, request));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(productRepository, times(1)).findById(productId);
        verify(fridgeRepository, times(0)).save(any(Fridge.class));
        verify(fridgeMapper, times(0)).toFridgeResponse(any(Fridge.class));
    }

    @Test
    void testRemoveProductFromFridgeWhenFridgeExistsAndProductExistsAndSufficientQuantity() {
        Long fridgeId = 1L;
        Long fridgeProductId = 1L;
        RemoveProductFromFridgeRequest request = new RemoveProductFromFridgeRequest(fridgeProductId, 5);

        Product product = new Product("Apple");
        Fridge fridge = new Fridge();
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);
        fridgeProduct.setFridgeProductId(fridgeProductId);
        fridge.getProducts().add(fridgeProduct);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        FridgeResponse fridgeResponse = new FridgeResponse();
        when(fridgeMapper.toFridgeResponse(fridge)).thenReturn(fridgeResponse);

        FridgeResponse result = fridgeService.removeProductFromFridge(fridgeId, request);

        assertNotNull(result);
        assertEquals(fridgeResponse, result);
        assertEquals(5, fridgeProduct.getQuantity());
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(fridgeProductRepository, times(1)).save(fridgeProduct);
        verify(fridgeRepository, times(1)).save(fridge);
        verify(fridgeMapper, times(1)).toFridgeResponse(fridge);
    }

    @Test
    void testRemoveProductFromFridgeWhenFridgeExistsAndProductQuantityBecomesZero() {
        Long fridgeId = 1L;
        Long fridgeProductId = 1L;
        RemoveProductFromFridgeRequest request = new RemoveProductFromFridgeRequest(fridgeProductId, 10);

        Product product = new Product("Apple");
        Fridge fridge = new Fridge();
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);
        fridgeProduct.setFridgeProductId(fridgeProductId);
        fridge.getProducts().add(fridgeProduct);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        FridgeResponse fridgeResponse = new FridgeResponse();
        when(fridgeMapper.toFridgeResponse(fridge)).thenReturn(fridgeResponse);

        FridgeResponse result = fridgeService.removeProductFromFridge(fridgeId, request);

        assertNotNull(result);
        assertEquals(fridgeResponse, result);
        assertEquals(0, fridgeProduct.getQuantity());
        assertFalse(fridge.getProducts().contains(fridgeProduct));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(fridgeProductRepository, times(1)).delete(fridgeProduct);
        verify(fridgeRepository, times(1)).save(fridge);
        verify(fridgeMapper, times(1)).toFridgeResponse(fridge);
    }

    @Test
    void testRemoveProductFromFridgeWhenFridgeDoesNotExist() {
        Long fridgeId = 1L;
        Long fridgeProductId = 1L;
        RemoveProductFromFridgeRequest request = new RemoveProductFromFridgeRequest(fridgeProductId, 5);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.empty());

        assertThrows(FridgeNotFoundException.class, () -> fridgeService.removeProductFromFridge(fridgeId, request));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(fridgeProductRepository, times(0)).delete(any(FridgeProduct.class));
        verify(fridgeProductRepository, times(0)).save(any(FridgeProduct.class));
        verify(fridgeRepository, times(0)).save(any(Fridge.class));
        verify(fridgeMapper, times(0)).toFridgeResponse(any(Fridge.class));
    }

    @Test
    void testRemoveProductFromFridgeWhenProductNotInFridge() {
        Long fridgeId = 1L;
        Long fridgeProductId = 1L;
        RemoveProductFromFridgeRequest request = new RemoveProductFromFridgeRequest(fridgeProductId, 5);

        Fridge fridge = new Fridge();

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));

        assertThrows(ProductNotFoundInFridgeException.class, () -> fridgeService.removeProductFromFridge(fridgeId, request));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(fridgeProductRepository, times(0)).delete(any(FridgeProduct.class));
        verify(fridgeProductRepository, times(0)).save(any(FridgeProduct.class));
        verify(fridgeRepository, times(0)).save(any(Fridge.class));
        verify(fridgeMapper, times(0)).toFridgeResponse(any(Fridge.class));
    }

    @Test
    void testRemoveProductFromFridgeWhenInsufficientQuantity() {
        Long fridgeId = 1L;
        Long fridgeProductId = 1L;
        RemoveProductFromFridgeRequest request = new RemoveProductFromFridgeRequest(fridgeProductId, 15);

        Product product = new Product("Apple");
        Fridge fridge = new Fridge();
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);
        fridgeProduct.setFridgeProductId(fridgeProductId);
        fridge.getProducts().add(fridgeProduct);

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));

        assertThrows(InsufficientQuantityException.class, () -> fridgeService.removeProductFromFridge(fridgeId, request));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(fridgeProductRepository, times(0)).delete(any(FridgeProduct.class));
        verify(fridgeProductRepository, times(0)).save(any(FridgeProduct.class));
        verify(fridgeRepository, times(0)).save(any(Fridge.class));
        verify(fridgeMapper, times(0)).toFridgeResponse(any(Fridge.class));
    }

    @Test
    void returnsRecipesWhenFridgeContainsNecessaryProducts() {
        Long fridgeId = 1L;
        Product product1 = new Product("Apple");
        Product product2 = new Product("Banana");

        FridgeProduct fridgeProduct1 = new FridgeProduct(QuantityType.PIECE, 10, null, product1);
        FridgeProduct fridgeProduct2 = new FridgeProduct(QuantityType.PIECE, 5, null, product2);
        Fridge fridge = new Fridge();
        fridge.getProducts().add(fridgeProduct1);
        fridge.getProducts().add(fridgeProduct2);

        Ingredient ingredient1 = new Ingredient(QuantityType.PIECE, 1, true, false, product1);
        Ingredient ingredient2 = new Ingredient(QuantityType.PIECE, 1, true, false, product2);

        Recipe recipe = new Recipe("Fruit Salad", "Delicious fruit salad", RecipeType.TEST, Weather.SNOWY, "http://recipe.url");
        recipe.getIngredients().add(ingredient1);
        recipe.getIngredients().add(ingredient2);

        List<Recipe> recipes = Collections.singletonList(recipe);
        RecipeResponse recipeResponse = new RecipeResponse();

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        when(recipeRepository.findAll()).thenReturn(recipes);
        when(recipeMapper.mapToRecipeResponseList(recipes)).thenReturn(Collections.singletonList(recipeResponse));

        List<RecipeResponse> result = fridgeService.getRecipesPossibleWithFridgeProducts(fridgeId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recipeResponse, result.getFirst());
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeMapper, times(1)).mapToRecipeResponseList(recipes);
    }

    @Test
    void throwsFridgeNotFoundExceptionWhenFridgeDoesNotExist() {
        Long fridgeId = 1L;

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.empty());

        assertThrows(FridgeNotFoundException.class, () -> fridgeService.getRecipesPossibleWithFridgeProducts(fridgeId));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(recipeRepository, times(0)).findAll();
        verify(recipeMapper, times(0)).mapToRecipeResponseList(anyList());
    }

    @Test
    void returnsMultipleRecipesWhenFridgeContainsAllNecessaryProducts() {
        Long fridgeId = 1L;
        Product product1 = new Product("Apple");
        Product product2 = new Product("Banana");

        FridgeProduct fridgeProduct1 = new FridgeProduct(QuantityType.PIECE, 10, null, product1);
        FridgeProduct fridgeProduct2 = new FridgeProduct(QuantityType.PIECE, 5, null, product2);
        Fridge fridge = new Fridge();
        fridge.getProducts().add(fridgeProduct1);
        fridge.getProducts().add(fridgeProduct2);

        Ingredient ingredient1 = new Ingredient(QuantityType.PIECE, 1, true, false, product1);
        Ingredient ingredient2 = new Ingredient(QuantityType.PIECE, 1, true, false, product2);

        Recipe recipe1 = new Recipe("Apple Pie", "Delicious apple pie", RecipeType.TEST, Weather.HOT, "http://recipe1.url");
        Recipe recipe2 = new Recipe("Fruit Salad", "Healthy fruit salad", RecipeType.AMERICAN, Weather.COLD, "http://recipe2.url");

        recipe1.getIngredients().add(ingredient1);
        recipe2.getIngredients().add(ingredient1);
        recipe2.getIngredients().add(ingredient2);

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        RecipeResponse response1 = new RecipeResponse();
        RecipeResponse response2 = new RecipeResponse();

        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        when(recipeRepository.findAll()).thenReturn(recipes);
        when(recipeMapper.mapToRecipeResponseList(recipes)).thenReturn(Arrays.asList(response1, response2));

        List<RecipeResponse> result = fridgeService.getRecipesPossibleWithFridgeProducts(fridgeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
        verify(fridgeRepository, times(1)).findById(fridgeId);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeMapper, times(1)).mapToRecipeResponseList(recipes);
    }
}
