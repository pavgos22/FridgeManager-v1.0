package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public List<UserResponse> mapToUsersList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }
}
