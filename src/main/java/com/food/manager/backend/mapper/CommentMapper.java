package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final ShoppingListItemMapper shoppingListItemMapper;

    public CommentMapper(ShoppingListItemMapper shoppingListItemMapper) {
        this.shoppingListItemMapper = shoppingListItemMapper;
    }

    public CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                shoppingListItemMapper.toShoppingListItemResponse(comment.getItem()),
                comment.getAuthor().getUserId()
        );
    }

    public List<CommentResponse> mapToCommentResponseList(List<Comment> comments) {
        return comments.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }
}
