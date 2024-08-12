package com.food.backend.service;

import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.entity.Comment;
import com.food.manager.backend.entity.ShoppingListItem;
import com.food.manager.backend.entity.User;
import com.food.manager.backend.exception.CommentNotFoundException;
import com.food.manager.backend.mapper.CommentMapper;
import com.food.manager.backend.repository.CommentRepository;
import com.food.manager.backend.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
class CommentServiceTestSuite {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentResponse commentResponse;
    private ShoppingListItem shoppingListItem;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        shoppingListItem = mock(ShoppingListItem.class);

        user = new User();
        user.setUserId(1L);
        user.setUsername("TestUser");

        comment = new Comment();
        comment.setCommentId(1L);
        comment.setContent("Test content");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setItem(shoppingListItem);
        comment.setAuthor(user);

        commentResponse = new CommentResponse(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                null,
                comment.getAuthor().getUserId()
        );
    }

    @Test
    void testGetComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentMapper.toCommentResponse(comment)).thenReturn(commentResponse);

        CommentResponse result = commentService.getComment(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCommentId());
        assertEquals("Test content", result.getContent());
        assertEquals(1L, result.getAuthorId());

        verify(commentRepository, times(1)).findById(1L);
        verify(commentMapper, times(1)).toCommentResponse(comment);
    }

    @Test
    void testGetComment_NotFound() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.getComment(1L));

        verify(commentRepository, times(1)).findById(1L);
        verify(commentMapper, never()).toCommentResponse(any());
    }

    @Test
    void testGetAllComments() {
        List<Comment> comments = Arrays.asList(comment);
        List<CommentResponse> commentResponses = Arrays.asList(commentResponse);

        when(commentRepository.findAll()).thenReturn(comments);
        when(commentMapper.mapToCommentResponseList(comments)).thenReturn(commentResponses);

        List<CommentResponse> result = commentService.getAllComments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCommentId());
        assertEquals("Test content", result.get(0).getContent());

        verify(commentRepository, times(1)).findAll();
        verify(commentMapper, times(1)).mapToCommentResponseList(comments);
    }

    @Test
    void testDeleteComment() {
        when(commentRepository.existsById(anyLong())).thenReturn(true);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteComment_NotFound() {
        when(commentRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(1L));

        verify(commentRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAllComments() {
        commentService.deleteAllComments();

        verify(commentRepository, times(1)).deleteAll();
    }
}
