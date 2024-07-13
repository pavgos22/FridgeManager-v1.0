package com.food.manager.controller;

import com.food.manager.dto.request.user.CreateUserRequest;
import com.food.manager.dto.request.user.UpdateUserRequest;
import com.food.manager.dto.response.UserResponse;
import com.food.manager.service.UserService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    public void setUp() {
        userResponse = new UserResponse(1L, "testuser", "Test", "User", "test.user@example.com", "password",
                LocalDateTime.now(), LocalDateTime.now(), List.of(), List.of());

        createUserRequest = new CreateUserRequest("testuser", "Test", "User", "test.user@example.com", "password");
        updateUserRequest = new UpdateUserRequest("updateduser", "Updated", "User", "updated.user@example.com", "newpassword");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserResponse> userResponses = Arrays.asList(userResponse);
        when(userService.getAllUsers()).thenReturn(userResponses);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test.user@example.com"));
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"firstName\":\"Test\",\"lastName\":\"User\",\"email\":\"test.user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test.user@example.com"));
    }

    @Test
    public void testGetUser() throws Exception {
        when(userService.getUser(1L)).thenReturn(Optional.of(userResponse));

        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test.user@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(userService.updateUser(anyLong(), any(UpdateUserRequest.class))).thenReturn(
                new UserResponse(
                        1L,
                        "updateduser",
                        "Updated",
                        "User",
                        "updated.user@example.com",
                        "newpassword",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of(),
                        List.of()
                )
        );

        mockMvc.perform(put("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updateduser\",\"firstName\":\"Updated\",\"lastName\":\"User\",\"email\":\"updated.user@example.com\",\"password\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated.user@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());
    }
}
