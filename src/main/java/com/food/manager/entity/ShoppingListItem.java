package com.food.manager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.food.manager.enums.QuantityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

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

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    @JsonBackReference
    private Product product;

    @OneToMany(mappedBy = "item")
    @JsonManagedReference
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", nullable = false)
    @JsonBackReference
    private Group group;

    public ShoppingListItem(Product product, QuantityType quantityType, int quantity, boolean checked, Group group) {
        this.product = product;
        this.quantityType = quantityType;
        this.quantity = quantity;
        this.checked = checked;
        this.group = group;
    }
}
