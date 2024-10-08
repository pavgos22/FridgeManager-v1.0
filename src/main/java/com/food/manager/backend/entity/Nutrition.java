package com.food.manager.backend.entity;

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
    private Product product;

    public Nutrition(int calories, float protein, float fat, float carbohydrate) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
    }
}