package com.food.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ITEM_ID", unique = true)
    private Long itemId;
    @Column(name="QUANTITY", nullable = true)
    private int quantity;
    @Column(name="CHECKED", nullable = false)
    private boolean checked;

    @OneToOne(mappedBy = "item")
    private Product product;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;
}
