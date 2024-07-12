package com.food.manager.entity;

import com.food.manager.enums.QuantityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "FRIDGE_PRODUCTS")
public class FridgeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="FRIDGE_PRODUCT_ID", unique=true)
    private Long FridgeProductId;
    @Column(name = "QUANTITY_TYPE", nullable = false)
    private QuantityType quantityType;
    @Column(name = "QUANTITY", nullable = false)
    private int quantity; //g/ml/szt. quantity type?

    @ManyToOne
    @JoinColumn(name="FRIDGE_ID")
    private Fridge fridge;

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID", nullable = false)
    private Product product;

    public FridgeProduct(QuantityType quantityType, int quantity, Fridge fridge, Product product) {
        this.quantityType = quantityType;
        this.quantity = quantity;
        this.fridge = fridge;
        this.product = product;
    }
}
