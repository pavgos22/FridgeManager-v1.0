package com.food.manager.mapper;

import com.food.manager.dto.response.CommentResponse;
import com.food.manager.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getItem(),
                comment.getAuthor().getUserId()
        );
    }

    public List<CommentResponse> mapToCommentResponseList(List<Comment> comments) {
        return comments.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }
}
