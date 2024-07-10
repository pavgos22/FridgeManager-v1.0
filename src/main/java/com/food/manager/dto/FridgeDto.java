package com.food.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FridgeDto {
    private Long fridgeId;
    private Long groupId;
    private List<Long> productIds;
}
