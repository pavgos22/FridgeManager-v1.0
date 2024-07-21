package com.food.manager.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
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

    @ManyToMany(mappedBy = "groups")
    @JsonManagedReference(value="group-user")
    private List<User> users;

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Fridge groupFridge;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ShoppingListItem> shoppingListItems;

    public Group(String groupName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.groupName = groupName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }
}
