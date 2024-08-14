package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.item.AddItemToListRequest;
import com.food.manager.backend.dto.request.item.RemoveItemFromListRequest;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.service.ShoppingListItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShoppingListItemController.class)
class ShoppingListItemControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingListItemService shoppingListItemService;

    private ShoppingListItemResponse shoppingListItemResponse;

    @BeforeEach
    void setUp() {
        shoppingListItemResponse = new ShoppingListItemResponse(1L, 1L, 1L, QuantityType.PIECE, 5, false);
    }

    @Test
    void getAllItemsSuccessfully() throws Exception {
        List<ShoppingListItemResponse> items = Arrays.asList(shoppingListItemResponse);
        Mockito.when(shoppingListItemService.getAllItems()).thenReturn(items);

        mockMvc.perform(get("/v1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].groupId").value(1L))
                .andExpect(jsonPath("$[0].quantityType").value("PIECE"))
                .andExpect(jsonPath("$[0].quantity").value(5))
                .andExpect(jsonPath("$[0].checked").value(false));
    }

    @Test
    void getItemSuccessfully() throws Exception {
        Mockito.when(shoppingListItemService.getItem(anyLong())).thenReturn(shoppingListItemResponse);

        mockMvc.perform(get("/v1/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.quantityType").value("PIECE"))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.checked").value(false));
    }

    @Test
    void addItemToShoppingListSuccessfully() throws Exception {
        AddItemToListRequest addItemToListRequest = new AddItemToListRequest(1L, 1L, QuantityType.PIECE, 5);

        Mockito.when(shoppingListItemService.addItemToShoppingList(any(AddItemToListRequest.class))).thenReturn(shoppingListItemResponse);

        mockMvc.perform(put("/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"groupId\":1,\"quantityType\":\"PIECE\",\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(1L))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.quantityType").value("PIECE"))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.checked").value(false));
    }

    @Test
    void removeItemFromShoppingListSuccessfully() throws Exception {
        RemoveItemFromListRequest removeItemFromListRequest = new RemoveItemFromListRequest(1L, 1L, 5);

        Mockito.doNothing().when(shoppingListItemService).removeItemFromShoppingList(any(RemoveItemFromListRequest.class));

        mockMvc.perform(delete("/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":1,\"groupId\":1,\"quantity\":5}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteItemSuccessfully() throws Exception {
        Mockito.doNothing().when(shoppingListItemService).deleteItem(anyLong());

        mockMvc.perform(delete("/v1/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllItemsSuccessfully() throws Exception {
        Mockito.doNothing().when(shoppingListItemService).deleteAllItems();

        mockMvc.perform(delete("/v1/items/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
