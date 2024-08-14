package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.group.*;
import com.food.manager.backend.dto.request.item.CreateItemRequest;
import com.food.manager.backend.dto.request.user.DeleteUserRequest;
import com.food.manager.backend.dto.response.GroupResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GroupControllerTestSuite {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private GroupResponse groupResponse;
    private UserResponse userResponse;
    private ShoppingListItemResponse shoppingListItemResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();

        userResponse = new UserResponse(1L, "username", "firstName", "lastName", "email@test.com", LocalDateTime.now(), LocalDateTime.now());
        shoppingListItemResponse = new ShoppingListItemResponse(1L, 1L, 1L, QuantityType.PIECE, 5, false);

        groupResponse = new GroupResponse();
        groupResponse.setGroupId(1L);
        groupResponse.setGroupName("Test Group");
        groupResponse.setCreatedAt(LocalDateTime.now());
        groupResponse.setUpdatedAt(LocalDateTime.now());
        groupResponse.setUsers(Arrays.asList(userResponse));
        groupResponse.setFridgeId(1L);
        groupResponse.setShoppingListItems(Arrays.asList(shoppingListItemResponse));
    }

    @Test
    void getAllGroupsSuccessfully() throws Exception {
        List<GroupResponse> groups = Arrays.asList(groupResponse);

        when(groupService.getAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].groupId").value(1L))
                .andExpect(jsonPath("$[0].groupName").value("Test Group"));
    }

    @Test
    void getGroupSuccessfully() throws Exception {
        when(groupService.getGroup(anyLong())).thenReturn(Optional.of(groupResponse));

        mockMvc.perform(get("/v1/groups/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void getGroupNotFound() throws Exception {
        when(groupService.getGroup(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/groups/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsersFromGroupSuccessfully() throws Exception {
        List<UserResponse> users = Arrays.asList(userResponse);

        when(groupService.getAllUsersInGroup(anyLong())).thenReturn(users);

        mockMvc.perform(get("/v1/groups/{id}/users", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].username").value("username"));
    }

    @Test
    void getGroupItemsSuccessfully() throws Exception {
        List<ShoppingListItemResponse> items = Arrays.asList(shoppingListItemResponse);

        when(groupService.getShoppingListItemsByGroup(anyLong())).thenReturn(items);

        mockMvc.perform(get("/v1/groups/{id}/items", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(1L));
    }

    @Test
    void createGroupSuccessfully() throws Exception {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest("Test Group");

        when(groupService.createGroup(any(CreateGroupRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(post("/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"groupName\":\"Test Group\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void updateGroupSuccessfully() throws Exception {
        UpdateGroupRequest updateGroupRequest = new UpdateGroupRequest("Updated Group");

        when(groupService.updateGroup(anyLong(), any(UpdateGroupRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(put("/v1/groups/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"groupName\":\"Updated Group\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void addUserSuccessfully() throws Exception {
        AddUserRequest addUserRequest = new AddUserRequest(1L, 1L);

        when(groupService.addUser(any(AddUserRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(post("/v1/groups/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"groupId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void removeUserSuccessfully() throws Exception {
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(1L, 1L);

        when(groupService.deleteUser(any(DeleteUserRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(delete("/v1/groups/removeUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"groupId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }

    @Test
    void addItemToGroupSuccessfully() throws Exception {
        CreateItemRequest createItemRequest = new CreateItemRequest(1L, QuantityType.PIECE, 5);

        when(groupService.addItemToGroup(anyLong(), any(CreateItemRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(put("/v1/groups/{id}/addItem", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":5,\"quantityType\":\"PIECE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }


    @Test
    void removeItemFromGroupSuccessfully() throws Exception {
        RemoveItemFromGroupRequest removeItemFromGroupRequest = new RemoveItemFromGroupRequest(1L, 5);

        when(groupService.removeItemFromGroup(anyLong(), any(RemoveItemFromGroupRequest.class))).thenReturn(groupResponse);

        mockMvc.perform(put("/v1/groups/{id}/removeItem", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":1, \"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1L))
                .andExpect(jsonPath("$.groupName").value("Test Group"));
    }


    @Test
    void deleteGroupSuccessfully() throws Exception {
        mockMvc.perform(delete("/v1/groups/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}