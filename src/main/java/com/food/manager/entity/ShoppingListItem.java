package com.food.manager.entity;

import com.food.manager.enums.QuantityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "SHOPPING_LIST_ITEMS")
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ITEM_ID", unique = true)
    private Long itemId;
    @Column(name="QUANTITY_TYPE", nullable = false)
    private QuantityType quantityType;
    @Column(name="QUANTITY", nullable = false)
    private int quantity;
    @Column(name="CHECKED", nullable = false)
    private boolean checked;

    @OneToOne
    @JoinColumn(name="PRODUCT_ID", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name="GROUP_ID", nullable = false)
    private Group group;
}