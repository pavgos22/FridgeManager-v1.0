package com.food.manager.mapper;

import com.food.manager.dto.response.UserResponse;
import com.food.manager.entity.User;

import java.util.List;

public class UserMapper {
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getGroups(),
                user.getComments()
        );
    }

    public List<UserResponse> mapToUsersList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }
}
