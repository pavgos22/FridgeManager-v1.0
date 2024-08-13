package com.food.manager.backend.entity;

import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
    @Column(name="RECIPE_NAME", nullable = false)
    private String recipeName;
    @Column(name="DESCRIPTION", nullable = true)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "RECIPE_TYPE", nullable = false)
    private RecipeType recipeType;
    @Enumerated(EnumType.STRING)
    @Column(name="WEATHER", nullable = true)
    private Weather weather;
    @Column(name="RECIPE_URL", nullable = true)
    private String recipeUrl;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<Ingredient> ingredients = new HashSet<>();

    public Recipe(String recipeName, String description, RecipeType recipeType, Weather weather, String recipeUrl) {
        this.recipeName = recipeName;
        this.description = description;
        this.recipeType = recipeType;
        this.weather = weather;
        this.recipeUrl = recipeUrl;
    }
}
