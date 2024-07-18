package com.food.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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

    @OneToMany(cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ShoppingListItem> items;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="NUTRITION_ID")
    private Nutrition nutrition;

    @ManyToMany
    @JoinTable(
            name = "PRODUCT_HAS_RECIPE",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "RECIPE_ID")
    )
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<FridgeProduct> fridgeProducts;

    public Product(String productName) {
        this.productName = productName;
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

