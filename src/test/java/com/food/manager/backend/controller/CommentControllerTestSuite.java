package com.food.manager.backend.controller;

import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private CommentResponse commentResponse;

    @BeforeEach
    void setUp() {
        ShoppingListItemResponse shoppingListItemResponse = new ShoppingListItemResponse(1L, 2L, 3L, QuantityType.PIECE, 5, false);

        commentResponse = new CommentResponse(1L, "Test Content", LocalDateTime.now().minusDays(1), LocalDateTime.now(), shoppingListItemResponse, 1L);
    }

    @Test
    void getCommentSuccessfully() throws Exception {
        when(commentService.getComment(anyLong())).thenReturn(commentResponse);

        mockMvc.perform(get("/v1/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.authorId").value(1L))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.item.itemId").value(1L))
                .andExpect(jsonPath("$.item.productId").value(2L))
                .andExpect(jsonPath("$.item.groupId").value(3L))
                .andExpect(jsonPath("$.item.quantityType").value("PIECE"))
                .andExpect(jsonPath("$.item.quantity").value(5))
                .andExpect(jsonPath("$.item.checked").value(false));
    }

    @Test
    void getAllCommentsSuccessfully() throws Exception {
        ShoppingListItemResponse anotherItemResponse = new ShoppingListItemResponse(2L, 3L, 4L, QuantityType.PIECE, 3, true);

        CommentResponse anotherCommentResponse = new CommentResponse(2L, "Another Content", LocalDateTime.now().minusDays(2), LocalDateTime.now().minusHours(5), anotherItemResponse, 2L);

        List<CommentResponse> comments = Arrays.asList(commentResponse, anotherCommentResponse);

        when(commentService.getAllComments()).thenReturn(comments);

        mockMvc.perform(get("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentId").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test Content"))
                .andExpect(jsonPath("$[0].authorId").value(1L))
                .andExpect(jsonPath("$[0].item.itemId").value(1L))
                .andExpect(jsonPath("$[0].item.productId").value(2L))
                .andExpect(jsonPath("$[0].item.groupId").value(3L))
                .andExpect(jsonPath("$[0].item.quantityType").value("PIECE"))
                .andExpect(jsonPath("$[0].item.quantity").value(5))
                .andExpect(jsonPath("$[0].item.checked").value(false))
                .andExpect(jsonPath("$[1].commentId").value(2L))
                .andExpect(jsonPath("$[1].content").value("Another Content"))
                .andExpect(jsonPath("$[1].authorId").value(2L))
                .andExpect(jsonPath("$[1].item.itemId").value(2L))
                .andExpect(jsonPath("$[1].item.productId").value(3L))
                .andExpect(jsonPath("$[1].item.groupId").value(4L))
                .andExpect(jsonPath("$[1].item.quantityType").value("PIECE"))
                .andExpect(jsonPath("$[1].item.quantity").value(3))
                .andExpect(jsonPath("$[1].item.checked").value(true));
    }

    @Test
    void deleteCommentSuccessfully() throws Exception {
        doNothing().when(commentService).deleteComment(anyLong());

        mockMvc.perform(delete("/v1/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllCommentsSuccessfully() throws Exception {
        doNothing().when(commentService).deleteAllComments();

        mockMvc.perform(delete("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
