package com.food.manager.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FridgeResponse {
    private Long fridgeId;
    private Long group;
    private List<FridgeProductResponse> fridgeProducts;
}
