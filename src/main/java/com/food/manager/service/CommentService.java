package com.food.manager.service;

import com.food.manager.dto.response.CommentResponse;
import com.food.manager.entity.Comment;
import com.food.manager.exception.CommentNotFoundException;
import com.food.manager.mapper.CommentMapper;
import com.food.manager.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    public CommentResponse getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        return commentMapper.toCommentResponse(comment);
    }

    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return commentMapper.mapToCommentResponseList(comments);
    }

    public void deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new CommentNotFoundException("Comment not found with id: " + id);
        }
    }

    public void deleteAllComments() {
        commentRepository.deleteAll();
    }
}
