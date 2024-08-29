package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.user.AddCommentRequest;
import com.food.manager.backend.dto.request.user.CreateUserRequest;
import com.food.manager.backend.dto.request.user.EditCommentRequest;
import com.food.manager.backend.dto.request.user.UpdateUserRequest;
import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.entity.Comment;
import com.food.manager.backend.entity.Group;
import com.food.manager.backend.entity.ShoppingListItem;
import com.food.manager.backend.entity.User;
import com.food.manager.backend.exception.*;
import com.food.manager.backend.mapper.CommentMapper;
import com.food.manager.backend.mapper.UserMapper;
import com.food.manager.backend.repository.CommentRepository;
import com.food.manager.backend.repository.GroupRepository;
import com.food.manager.backend.repository.ShoppingListItemRepository;
import com.food.manager.backend.repository.UserRepository;
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
    @Autowired
    private GroupRepository groupRepository;

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
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public CommentResponse addComment(Long authorId, AddCommentRequest addCommentRequest) {
        ShoppingListItem item = shoppingListItemRepository.findById(addCommentRequest.itemId()).orElseThrow(() -> new ShoppingListItemNotFoundException(addCommentRequest.itemId()));
        User user = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        Group group = groupRepository.findByItem(addCommentRequest.itemId());

        if(!group.getUsers().contains(user))
            throw new UserNotInGroupException(authorId, group.getGroupId());

        Comment comment = new Comment(
                addCommentRequest.content(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                item, user
        );
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public CommentResponse editComment(Long commentId, EditCommentRequest editCommentRequest) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        if(!comment.getAuthor().getUserId().equals(editCommentRequest.AuthorId()))
            throw new NotUsersCommentException(editCommentRequest.AuthorId(), commentId);
        comment.setContent(editCommentRequest.content());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
