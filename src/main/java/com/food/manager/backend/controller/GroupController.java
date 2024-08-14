package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.group.*;
import com.food.manager.backend.dto.request.item.CreateItemRequest;
import com.food.manager.backend.dto.request.user.DeleteUserRequest;
import com.food.manager.backend.dto.response.GroupResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long id) {
        Optional<GroupResponse> groupResponse = groupService.getGroup(id);
        return groupResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserResponse>> getAllUsersFromGroup(@PathVariable Long id) {
        List<UserResponse> groupUsers = groupService.getAllUsersInGroup(id);
        return ResponseEntity.ok(groupUsers);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ShoppingListItemResponse>> getGroupItems(@PathVariable Long id) {
        List<ShoppingListItemResponse> items = groupService.getShoppingListItemsByGroup(id);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@RequestBody CreateGroupRequest createGroupRequest) {
        GroupResponse groupResponse = groupService.createGroup(createGroupRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @RequestBody UpdateGroupRequest updateGroupRequest) {
        GroupResponse groupResponse = groupService.updateGroup(id, updateGroupRequest);
        return ResponseEntity.ok(groupResponse);
    }


    @PostMapping("/addUser")
    public ResponseEntity<GroupResponse> addUser(@RequestBody AddUserRequest addUserRequest) {
        GroupResponse groupResponse = groupService.addUser(addUserRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @DeleteMapping("/removeUser")
    public ResponseEntity<GroupResponse> removeUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        GroupResponse groupResponse = groupService.deleteUser(deleteUserRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @PutMapping("/{id}/addItem")
    public ResponseEntity<GroupResponse> addItemToGroup(@PathVariable Long id, @RequestBody CreateItemRequest createItemRequest) {
        GroupResponse groupResponse = groupService.addItemToGroup(id, createItemRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @PutMapping("/{id}/removeItem")
    public ResponseEntity<GroupResponse> removeItemFromGroup(@PathVariable Long id, @RequestBody RemoveItemFromGroupRequest removeItemFromGroupRequest) {
        GroupResponse groupResponse = groupService.removeItemFromGroup(id, removeItemFromGroupRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}