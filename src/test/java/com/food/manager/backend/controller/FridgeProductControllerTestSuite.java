package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.FridgeProductResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.service.FridgeProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FridgeProductControllerTestSuite {

    private MockMvc mockMvc;

    @Mock
    private FridgeProductService fridgeProductService;

    @InjectMocks
    private FridgeProductController fridgeProductController;

    private FridgeProductResponse fridgeProductResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fridgeProductController).build();

        fridgeProductResponse = new FridgeProductResponse();
        fridgeProductResponse.setFridgeProductId(1L);
        fridgeProductResponse.setQuantityType(QuantityType.PIECE);
        fridgeProductResponse.setQuantity(5);
        fridgeProductResponse.setProductName("Milk");
    }

    @Test
    void getFridgeProductSuccessfully() throws Exception {
        when(fridgeProductService.getFridgeProduct(anyLong())).thenReturn(fridgeProductResponse);

        mockMvc.perform(get("/v1/fridgeProducts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fridgeProductId").value(1L))
                .andExpect(jsonPath("$.productName").value("Milk"))
                .andExpect(jsonPath("$.quantityType").value("PIECE"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void getAllFridgeProductsSuccessfully() throws Exception {
        List<FridgeProductResponse> fridgeProducts = Arrays.asList(fridgeProductResponse);

        when(fridgeProductService.getAllFridgeProducts()).thenReturn(fridgeProducts);

        mockMvc.perform(get("/v1/fridgeProducts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fridgeProductId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Milk"))
                .andExpect(jsonPath("$[0].quantityType").value("PIECE"))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }
}
