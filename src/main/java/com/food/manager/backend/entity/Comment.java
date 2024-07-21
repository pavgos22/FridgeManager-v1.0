package com.food.manager.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMMENT_ID", unique = true)
    private Long commentId;

    @Column(name="CONTENT", nullable = false)
    private String content;

    @Column(name="CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT", nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="ITEM_ID", nullable = false)
    @JsonBackReference
    private ShoppingListItem item;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    @JsonBackReference(value="user-comment")
    private User author;

    public Comment(String content, LocalDateTime createdAt, LocalDateTime updatedAt, ShoppingListItem item, User author) {
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.item = item;
        this.author = author;
    }
}
