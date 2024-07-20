package com.food.manager.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "WISHLIST")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="PRODUCT_NAME", nullable=false)
    private String productName;

    public Wishlist(String productName) {
        this.productName = productName;
    }
}
