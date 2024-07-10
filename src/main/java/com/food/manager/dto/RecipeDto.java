package com.food.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RecipeDto {
    private Long recipeId;
    private String description;
    private int numberOfServings;
    private String recipeType;
    private String weather;
    private List<Long> productIds;
}
