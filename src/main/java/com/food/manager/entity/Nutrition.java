package com.food.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "NUTRITION")
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nutritionId;

    @Column(name = "CALORIES", nullable = false)
    private int calories;
    @Column(name = "PROTEIN", nullable = false)
    private float protein;
    @Column(name = "FAT", nullable = false)
    private float fat;
    @Column(name = "CARBOHYDRATE", nullable = false)
    private float carbohydrate;

    @OneToOne(mappedBy = "nutrition")
    @JsonBackReference
    private Product product;
}