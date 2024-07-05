package com.food.manager.entity;

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
    @Column(name = "QUANTITY_TYPE")
    private String quantityType;
    @Column(name = "QUANTITY")
    private int quantity; //g/ml/szt. quantity type?

    @ManyToOne
    @JoinColumn(name="FRIDGE_ID", nullable=false)
    private Fridge fridge;

    @OneToOne(mappedBy = "fridgeProduct")
    private Product product;

}
