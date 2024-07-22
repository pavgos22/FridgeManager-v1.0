package com.food.manager.backend.dto.response;

import java.time.LocalDateTime;

public class UserResponse {

    public Long userId;
    public String username;
    public String firstName;
    public String lastName;
    public String email;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

//    @JsonBackReference(value = "group-user")
//    private List<Long> groupIds;
//
//    private List<Comment> comments;

    public UserResponse(Long userId, String username, String firstName, String lastName, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long userId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String username() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String firstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String lastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String email() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

//    public List<Long> groupIds() {
//        return groupIds;
//    }
//
//    public void setGroupIds(List<Long> groupIds) {
//        this.groupIds = groupIds;
//    }
//
//    public List<Comment> comments() {
//        return comments;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }
}
