package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.group.*;
import com.food.manager.backend.dto.request.item.CreateItemRequest;
import com.food.manager.backend.dto.request.user.DeleteUserRequest;
import com.food.manager.backend.dto.response.GroupResponse;
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
        List<GroupResponse> groups = groupService.getAllUsers();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long id) {
        Optional<GroupResponse> groupResponse = groupService.getGroup(id);
        return groupResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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

    @PutMapping("/removeUser")
    public ResponseEntity<GroupResponse> removeUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        GroupResponse groupResponse = groupService.deleteUser(deleteUserRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @PutMapping("/addItem")
    public ResponseEntity<GroupResponse> addItemToGroup(@RequestBody CreateItemRequest createItemRequest) {
        GroupResponse groupResponse = groupService.addItemToGroup(createItemRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @PutMapping("/removeItem")
    public ResponseEntity<GroupResponse> removeItemFromGroup(
            @RequestBody RemoveItemFromGroupRequest removeItemFromGroupRequest) {
        GroupResponse groupResponse = groupService.removeItemFromGroup(removeItemFromGroupRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest(id);
        groupService.deleteGroup(deleteGroupRequest);
        return ResponseEntity.noContent().build();
    }
}