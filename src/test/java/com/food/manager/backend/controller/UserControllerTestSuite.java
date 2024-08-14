package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.user.AddCommentRequest;
import com.food.manager.backend.dto.request.user.CreateUserRequest;
import com.food.manager.backend.dto.request.user.EditCommentRequest;
import com.food.manager.backend.dto.request.user.UpdateUserRequest;
import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private CommentResponse commentResponse;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse(1L, "testuser", "Test", "User", "testuser@example.com", LocalDateTime.now(), LocalDateTime.now());

        ShoppingListItemResponse shoppingListItemResponse = new ShoppingListItemResponse(1L, 1L, 1L, QuantityType.PIECE, 5, false);
        commentResponse = new CommentResponse(1L, "Test comment", LocalDateTime.now(), LocalDateTime.now(), shoppingListItemResponse, 1L);
    }

    @Test
    void getAllUsersSuccessfully() throws Exception {
        List<UserResponse> users = Arrays.asList(userResponse);
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].firstName").value("Test"))
                .andExpect(jsonPath("$[0].lastName").value("User"))
                .andExpect(jsonPath("$[0].email").value("testuser@example.com"));
    }

    @Test
    void createUserSuccessfully() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("testuser", "Test", "User", "testuser@example.com", "password");

        Mockito.when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"firstName\":\"Test\",\"lastName\":\"User\",\"email\":\"testuser@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void getUserSuccessfully() throws Exception {
        Mockito.when(userService.getUser(anyLong())).thenReturn(Optional.of(userResponse));

        mockMvc.perform(get("/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void addCommentSuccessfully() throws Exception {
        AddCommentRequest addCommentRequest = new AddCommentRequest(1L, "Test comment");

        Mockito.when(userService.addComment(anyLong(), any(AddCommentRequest.class))).thenReturn(commentResponse);

        mockMvc.perform(post("/v1/users/{id}/addComment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":1,\"content\":\"Test comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.content").value("Test comment"))
                .andExpect(jsonPath("$.authorId").value(1L));
    }

    @Test
    void editCommentSuccessfully() throws Exception {
        CommentResponse updatedCommentResponse = new CommentResponse(1L, "Updated comment", LocalDateTime.now(), LocalDateTime.now(), new ShoppingListItemResponse(1L, 1L, 1L, QuantityType.PIECE, 5, false), 1L);

        Mockito.when(userService.editComment(anyLong(), any(EditCommentRequest.class))).thenReturn(updatedCommentResponse);

        mockMvc.perform(put("/v1/users/{id}/editComment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"AuthorId\":1,\"content\":\"Updated comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.content").value("Updated comment"))
                .andExpect(jsonPath("$.authorId").value(1L));
    }


    @Test
    void updateUserSuccessfully() throws Exception {
        UserResponse updatedUserResponse = new UserResponse(1L, "updatedUser", "Updated", "User", "updateduser@example.com", LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(userService.updateUser(anyLong(), any(UpdateUserRequest.class))).thenReturn(updatedUserResponse);

        mockMvc.perform(put("/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\",\"firstName\":\"Updated\",\"lastName\":\"User\",\"email\":\"updateduser@example.com\",\"password\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("updatedUser"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("updateduser@example.com"));
    }


    @Test
    void deleteUserSuccessfully() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}