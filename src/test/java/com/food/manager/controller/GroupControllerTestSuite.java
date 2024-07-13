package com.food.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.manager.dto.request.group.AddUserRequest;
import com.food.manager.dto.request.group.CreateGroupRequest;
import com.food.manager.dto.request.group.UpdateGroupRequest;
import com.food.manager.dto.request.user.DeleteUserRequest;
import com.food.manager.dto.response.GroupResponse;
import com.food.manager.entity.ShoppingListItem;
import com.food.manager.entity.User;
import com.food.manager.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
public class GroupControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

    private GroupResponse groupResponse;

    @BeforeEach
    void setUp() {
        groupResponse = new GroupResponse(
                1L, "Test Group", LocalDateTime.now(), LocalDateTime.now(),
                Arrays.asList(new User()), null, Arrays.asList(new ShoppingListItem())
        );
    }

    @Test
    void testGetAllGroups() throws Exception {
        when(groupService.getAllUsers()).thenReturn(Arrays.asList(groupResponse));

        mockMvc.perform(get("/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].groupName").value("Test Group"));
    }

    @Test
    void testGetGroup() throws Exception {
        when(groupService.getGroup(anyLong())).thenReturn(Optional.of(groupResponse));

        mockMvc.perform(get("/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void testCreateGroup() throws Exception {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest("New Group");
        when(groupService.createGroup(any(CreateGroupRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(post("/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createGroupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void testUpdateGroup() throws Exception {
        UpdateGroupRequest updateGroupRequest = new UpdateGroupRequest(1L, "Updated Group");
        GroupResponse groupResponse = new GroupResponse(1L, "Updated Group", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>(), null, new ArrayList<>());

        when(groupService.updateGroup(any(UpdateGroupRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(put("/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateGroupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("Updated Group"));
    }


    @Test
    void testAddUser() throws Exception {
        AddUserRequest addUserRequest = new AddUserRequest(1L, 1L);
        when(groupService.addUser(any(AddUserRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(post("/v1/groups/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void testDeleteUser() throws Exception {
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(1L);
        when(groupService.deleteUser(any(DeleteUserRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(post("/v1/groups/deleteUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void testDeleteGroup() throws Exception {
        mockMvc.perform(delete("/v1/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
