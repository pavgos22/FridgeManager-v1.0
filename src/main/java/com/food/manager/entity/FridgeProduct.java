package com.food.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class FridgeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="FRIDGE_PRODUCT_ID", unique=true)
    private Long FridgeProductId;
    @Column(name = "QUANTITY")
    private int quantity; //g/ml/szt. quantity type?

    @ManyToOne
    @JoinColumn(name="FRIDGE_ID", nullable=false)
    private Fridge fridge;

    @OneToOne(mappedBy = "fridgeProduct")
    private Product product;

}
