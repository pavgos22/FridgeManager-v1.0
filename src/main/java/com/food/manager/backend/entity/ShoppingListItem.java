package com.food.manager.backend.entity;

import com.food.manager.backend.enums.QuantityType;
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
@Table(name = "SHOPPING_LIST_ITEMS")
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ITEM_ID", unique = true)
    private Long itemId;
    @Enumerated(EnumType.STRING)
    @Column(name="QUANTITY_TYPE", nullable = false)
    private QuantityType quantityType;
    @Column(name="QUANTITY", nullable = false)
    private int quantity;
    @Column(name="CHECKED", nullable = false)
    private boolean checked;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private Group group;

    public ShoppingListItem(Product product, QuantityType quantityType, int quantity, boolean checked, Group group) {
        this.product = product;
        this.quantityType = quantityType;
        this.quantity = quantity;
        this.checked = checked;
        this.group = group;
    }
}
