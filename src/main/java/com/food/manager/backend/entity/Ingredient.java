package com.food.manager.backend.entity;

import com.food.manager.backend.enums.QuantityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name="INGREDIENTS")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="INGREDIENT_ID", unique=true)
    private Long ingredientId;
    @Column(name="QUANTITY_TYPE")
    private QuantityType quantityType;
    @Column(name="QUANTITY")
    private int quantity;
    @Column(name="REQUIRED")
    private boolean required;
    @Column(name="IGNORE_GROUP")
    private boolean ignoreGroup;

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name="RECIPE_ID")
    private Recipe recipe;

    public Ingredient(QuantityType quantityType, int quantity, boolean required, boolean ignoreGroup, Product product) {
        this.quantityType = quantityType;
        this.quantity = quantity;
        this.required = required;
        this.ignoreGroup = ignoreGroup;
        this.product = product;
    }
}
