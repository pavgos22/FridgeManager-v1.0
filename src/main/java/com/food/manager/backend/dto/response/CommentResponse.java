package com.food.manager.backend.dto.response;

import com.food.manager.backend.entity.ShoppingListItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ShoppingListItem item;
    private Long authorId;
}
