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
public class Fridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="FRIDGE_ID", unique = true)
    private Long fridgeId;

    @OneToOne
    @JoinColumn(name = "GROUP_ID", nullable = true)
    private Group group;

    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FridgeProduct> products = new ArrayList<>();
}

