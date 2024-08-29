package com.food.manager.backend.service;

import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.entity.Comment;
import com.food.manager.backend.exception.CommentNotFoundException;
import com.food.manager.backend.mapper.CommentMapper;
import com.food.manager.backend.repository.CommentRepository;
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
                .orElseThrow(() -> new CommentNotFoundException(id));
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
            throw new CommentNotFoundException(id);
        }
    }

    public void deleteAllComments() {
        commentRepository.deleteAll();
    }
}
