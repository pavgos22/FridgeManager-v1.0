package com.food.manager.service;

import com.food.manager.dto.request.user.AddCommentRequest;
import com.food.manager.dto.request.user.CreateUserRequest;
import com.food.manager.dto.request.user.EditCommentRequest;
import com.food.manager.dto.request.user.UpdateUserRequest;
import com.food.manager.dto.response.CommentResponse;
import com.food.manager.dto.response.UserResponse;
import com.food.manager.entity.Comment;
import com.food.manager.entity.ShoppingListItem;
import com.food.manager.entity.User;
import com.food.manager.mapper.CommentMapper;
import com.food.manager.mapper.UserMapper;
import com.food.manager.repository.CommentRepository;
import com.food.manager.repository.ShoppingListItemRepository;
import com.food.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentRepository commentRepository;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.mapToUsersList(users);
    }

    public UserResponse createUser(CreateUserRequest userRequest) {
        User user = new User(
                userRequest.username(),
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.email(),
                userRequest.password(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    public Optional<UserResponse> getUser(Long userId) {
        return userRepository.findById(userId).map(userMapper::toUserResponse);
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest userRequest) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setUsername(userRequest.username());
                    user.setFirstName(userRequest.firstName());
                    user.setLastName(userRequest.lastName());
                    user.setEmail(userRequest.email());
                    user.setPassword(userRequest.password());
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public CommentResponse addComment(AddCommentRequest addCommentRequest) {
        ShoppingListItem item = shoppingListItemRepository.findById(addCommentRequest.itemId()).orElseThrow(() -> new RuntimeException("Item not found"));
        User user = userRepository.findById(addCommentRequest.userId()).orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = new Comment(
                addCommentRequest.content(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                item, user
        );
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public CommentResponse editComment(EditCommentRequest editCommentRequest) {
        Comment comment = commentRepository.findById(editCommentRequest.commentId()).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(editCommentRequest.content());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
