package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long id) {
        CommentResponse comment = commentService.getComment(id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllComments() {
        commentService.deleteAllComments();
        return ResponseEntity.noContent().build();
    }
}
