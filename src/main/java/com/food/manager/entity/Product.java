package com.food.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PRODUCT_ID", unique=true)
    private Long productId;
    @Column(name = "PRODUCT_NAME", nullable=false)
    private String productName;

    @OneToOne
    @JoinColumn(name="NUTRITION_ID", nullable = true)
    private Nutrition nutrition;

    @OneToOne
    @JoinColumn(name="ITEM_ID", nullable = true)
    private ShoppingListItem item;

    @ManyToMany
    @JoinTable(
            name = "PRODUCT_HAS_RECIPE",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "RECIPE_ID")
    )
    private List<Recipe> recipes;

    @OneToMany
    private List<FridgeProduct> fridgeProductsList;

    public Product(String productName) {
        this.productName = productName;
    }
}
