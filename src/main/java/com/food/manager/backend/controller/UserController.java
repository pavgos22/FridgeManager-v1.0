package com.food.manager.backend.controller;

import com.food.manager.backend.dto.request.user.AddCommentRequest;
import com.food.manager.backend.dto.request.user.CreateUserRequest;
import com.food.manager.backend.dto.request.user.EditCommentRequest;
import com.food.manager.backend.dto.request.user.UpdateUserRequest;
import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest userRequest) { //userExistsException
        UserResponse newUser = userService.createUser(userRequest);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/addComment")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id, @RequestBody AddCommentRequest addCommentRequest) {
        CommentResponse comment = userService.addComment(id, addCommentRequest);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("{id}/editComment")
    public ResponseEntity<CommentResponse> editComment(@PathVariable Long id, @RequestBody EditCommentRequest editCommentRequest) {
        CommentResponse updatedComment = userService.editComment(id, editCommentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}