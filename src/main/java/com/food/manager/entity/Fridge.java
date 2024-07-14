package com.food.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "FRIDGES")
public class Fridge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="FRIDGE_ID", unique = true)
    private Long fridgeId;

    @OneToOne
    @JoinColumn(name = "GROUP_ID")
    @JsonBackReference
    private Group group;

    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FridgeProduct> products;

    public Fridge(Group group) {
        this.group = group;
    }
}