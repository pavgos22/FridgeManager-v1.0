package com.food.manager.controller;

import com.food.manager.dto.request.group.AddUserRequest;
import com.food.manager.dto.request.group.CreateGroupRequest;
import com.food.manager.dto.request.group.DeleteGroupRequest;
import com.food.manager.dto.request.group.UpdateGroupRequest;
import com.food.manager.dto.request.user.DeleteUserRequest;
import com.food.manager.dto.response.GroupResponse;
import com.food.manager.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
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
    public ResponseEntity<GroupResponse> addUserToGroup(@RequestBody AddUserRequest addUserRequest) {
        GroupResponse groupResponse = groupService.addUser(addUserRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<GroupResponse> deleteUserFromGroup(@RequestBody DeleteUserRequest deleteUserRequest) {
        GroupResponse groupResponse = groupService.deleteUser(deleteUserRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest(id);
        groupService.deleteGroup(deleteGroupRequest);
        return ResponseEntity.noContent().build();
    }
}