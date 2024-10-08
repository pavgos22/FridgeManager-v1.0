package com.food.manager.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "`GROUPS`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_ID", unique = true)
    private Long groupId;
    @Column(name = "GROUP_NAME", nullable = false)
    private String groupName;
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Fridge groupFridge;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShoppingListItem> shoppingListItems = new ArrayList<>();

    public Group(String groupName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.groupName = groupName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }
}
