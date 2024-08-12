package com.food.manager.backend.service;

import com.food.manager.backend.dto.response.FridgeProductResponse;
import com.food.manager.backend.entity.Fridge;
import com.food.manager.backend.entity.FridgeProduct;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.mapper.FridgeProductMapper;
import com.food.manager.backend.repository.FridgeProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FridgeProductServiceTestSuite {

    @Mock
    private FridgeProductRepository fridgeProductRepository;

    @Mock
    private FridgeProductMapper fridgeProductMapper;

    @InjectMocks
    private FridgeProductService fridgeProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFridgeProductWhenFridgeAndProductAreNotNull() {
        Fridge fridge = new Fridge();
        Product product = new Product("Apple");
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, product);
        FridgeProductResponse response = new FridgeProductResponse();

        when(fridgeProductRepository.findById(1L)).thenReturn(Optional.of(fridgeProduct));
        when(fridgeProductMapper.toFridgeProductResponse(fridgeProduct)).thenReturn(response);

        FridgeProductResponse result = fridgeProductService.getFridgeProduct(1L);

        assertNotNull(result);
        verify(fridgeProductRepository, times(1)).findById(1L);
        verify(fridgeProductMapper, times(1)).toFridgeProductResponse(fridgeProduct);
    }

    @Test
    void testGetFridgeProductWhenFridgeIsNull() {
        Product product = new Product("Apple");
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, null, product);
        FridgeProductResponse response = new FridgeProductResponse();

        when(fridgeProductRepository.findById(1L)).thenReturn(Optional.of(fridgeProduct));
        when(fridgeProductMapper.toFridgeProductResponse(fridgeProduct)).thenReturn(response);

        FridgeProductResponse result = fridgeProductService.getFridgeProduct(1L);

        assertNotNull(result);
        verify(fridgeProductRepository, times(1)).findById(1L);
        verify(fridgeProductMapper, times(1)).toFridgeProductResponse(fridgeProduct);
    }

    @Test
    void testGetFridgeProductWhenProductIsNull() {
        Fridge fridge = new Fridge();
        FridgeProduct fridgeProduct = new FridgeProduct(QuantityType.PIECE, 10, fridge, null);

        when(fridgeProductRepository.findById(1L)).thenReturn(Optional.of(fridgeProduct));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fridgeProductService.getFridgeProduct(1L);
        });

        assertEquals("FridgeProduct has a null Product with id: 1", exception.getMessage());
        verify(fridgeProductRepository, times(1)).findById(1L);
        verify(fridgeProductMapper, times(0)).toFridgeProductResponse(any(FridgeProduct.class));
    }


    @Test
    void testGetAllFridgeProducts() {
        FridgeProduct fridgeProduct1 = new FridgeProduct(QuantityType.PIECE, 10, new Fridge(), new Product("Apple"));
        FridgeProduct fridgeProduct2 = new FridgeProduct(QuantityType.PIECE, 5, null, new Product("Orange"));
        List<FridgeProduct> fridgeProductList = Arrays.asList(fridgeProduct1, fridgeProduct2);

        FridgeProductResponse response1 = new FridgeProductResponse();
        FridgeProductResponse response2 = new FridgeProductResponse();

        when(fridgeProductRepository.findAll()).thenReturn(fridgeProductList);
        when(fridgeProductMapper.mapToFridgeProductList(fridgeProductList)).thenReturn(Arrays.asList(response1, response2));

        List<FridgeProductResponse> result = fridgeProductService.getAllFridgeProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fridgeProductRepository, times(1)).findAll();
        verify(fridgeProductMapper, times(1)).mapToFridgeProductList(fridgeProductList);
    }
}
