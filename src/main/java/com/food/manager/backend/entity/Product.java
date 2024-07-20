package com.food.manager.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.food.manager.backend.enums.ProductGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Column(name = "PRODUCT_NAME", nullable=false, unique = true)
    private String productName;
    @Column(name="PRODUCT_GROUP")
    private ProductGroup productGroup;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ShoppingListItem> items;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="NUTRITION_ID")
    private Nutrition nutrition;

    @OneToMany(mappedBy ="product", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<FridgeProduct> fridgeProducts;

    public Product(String productName) {
        this.productName = productName;
    }

    public Product(String productName, ProductGroup productGroup) {
        this.productName = productName;
        this.productGroup = productGroup;
    }

    public List<Long> getRecipeIds() {
        return ingredients.stream()
                .map(Ingredient::getRecipe)
                .filter(Objects::nonNull)
                .map(Recipe::getRecipeId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}

