package com.food.manager.entity;

import com.food.manager.enums.RecipeType;
import com.food.manager.enums.Weather;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "RECIPES")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="RECIPE_ID", unique = true)
    private Long recipeId;
    @Column(name="DESCRIPTION", nullable = true)
    private String description;
    @Column(name="NUMBER_OF_SERVINGS", nullable = false)
    private int numberOfServings;
    @Column(name = "RECIPE_TYPE", nullable = false)
    private RecipeType recipeType;
    @Column(name="WEATHER", nullable = true)
    private Weather weather;

    @ManyToMany(mappedBy = "recipes") //ingredients
    private List<Product> products;
}
