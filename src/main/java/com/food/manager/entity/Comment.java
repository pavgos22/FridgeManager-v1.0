package com.food.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMMENT_ID", unique = true)
    private int commentId;
    @Column(name="CONTENT", nullable = false)
    private String content;
    @Column(name="CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
    @Column(name="UPDATED_AT", nullable = true)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="ITEM_ID")
    private ShoppingListItem item;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User author;
}
