package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.user.AddCommentRequest;
import com.food.manager.backend.dto.request.user.CreateUserRequest;
import com.food.manager.backend.dto.request.user.EditCommentRequest;
import com.food.manager.backend.dto.request.user.UpdateUserRequest;
import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.entity.*;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.exception.NotUsersCommentException;
import com.food.manager.backend.exception.UserNotInGroupException;
import com.food.manager.backend.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class UserServiceTestSuite {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProductRepository productRepository;

    private User testUser;
    private User anotherUser;
    private Group testGroup;
    private ShoppingListItem testItem;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        shoppingListItemRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();

        String uniqueUsername = "test-username-" + UUID.randomUUID();
        String anotherUniqueUsername = "another-username-" + UUID.randomUUID();

        testUser = new User(uniqueUsername, "firstName", "lastName", "email@test.com", "password", LocalDateTime.now(), LocalDateTime.now());
        testUser = userRepository.save(testUser);

        anotherUser = new User(anotherUniqueUsername, "anotherFirstName", "anotherLastName", "anotherEmail@test.com", "password", LocalDateTime.now(), LocalDateTime.now());
        anotherUser = userRepository.save(anotherUser);

        testGroup = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        testGroup.getUsers().add(testUser);
        testGroup.getUsers().add(anotherUser);
        testGroup = groupRepository.save(testGroup);

        Product testProduct = new Product("Test Product");
        testProduct = productRepository.save(testProduct);

        testItem = new ShoppingListItem(testProduct, QuantityType.PIECE, 5, false, testGroup);
        testItem = shoppingListItemRepository.save(testItem);

        testComment = new Comment("Initial comment", LocalDateTime.now(), LocalDateTime.now(), testItem, testUser);
        testComment = commentRepository.save(testComment);
    }

    @Test
    void createUserSuccessfully() {
        String uniqueUsername = "test-username-" + UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest(uniqueUsername, "firstName", "lastName", "email@test.com", "password");

        UserResponse userResponse = userService.createUser(request);

        assertNotNull(userResponse);
        assertEquals(uniqueUsername, userResponse.getUsername());
        assertEquals("firstName", userResponse.getFirstName());
        assertEquals("lastName", userResponse.getLastName());
        assertEquals("email@test.com", userResponse.getEmail());

        Optional<User> savedUser = userRepository.findById(userResponse.getUserId());
        assertTrue(savedUser.isPresent());
        assertEquals(uniqueUsername, savedUser.get().getUsername());
        assertEquals("firstName", savedUser.get().getFirstName());
        assertEquals("lastName", savedUser.get().getLastName());
        assertEquals("email@test.com", savedUser.get().getEmail());
    }

    @Test
    void getUserSuccessfully() {
        String uniqueUsername = "test-username-" + UUID.randomUUID();
        User user = new User(uniqueUsername, "firstName", "lastName", "email@test.com", "password", LocalDateTime.now(), LocalDateTime.now());
        User savedUser = userRepository.save(user);

        Optional<UserResponse> userResponse = userService.getUser(savedUser.getUserId());

        assertTrue(userResponse.isPresent());
        assertEquals(savedUser.getUserId(), userResponse.get().getUserId());
        assertEquals(savedUser.getUsername(), userResponse.get().getUsername());
        assertEquals(savedUser.getFirstName(), userResponse.get().getFirstName());
        assertEquals(savedUser.getLastName(), userResponse.get().getLastName());
        assertEquals(savedUser.getEmail(), userResponse.get().getEmail());
    }

    @Test
    void updateUserSuccessfully() {
        String uniqueUsername = "test-username-" + UUID.randomUUID();
        User user = new User(uniqueUsername, "firstName", "lastName", "email@test.com", "password", LocalDateTime.now(), LocalDateTime.now());
        User savedUser = userRepository.save(user);

        UpdateUserRequest updateRequest = new UpdateUserRequest("newUsername", "newFirstName", "newLastName", "newEmail@test.com", "newPassword");

        UserResponse updatedUserResponse = userService.updateUser(savedUser.getUserId(), updateRequest);

        assertNotNull(updatedUserResponse);
        assertEquals("newUsername", updatedUserResponse.getUsername());
        assertEquals("newFirstName", updatedUserResponse.getFirstName());
        assertEquals("newLastName", updatedUserResponse.getLastName());
        assertEquals("newEmail@test.com", updatedUserResponse.getEmail());

        Optional<User> updatedUser = userRepository.findById(savedUser.getUserId());
        assertTrue(updatedUser.isPresent());
        assertEquals("newUsername", updatedUser.get().getUsername());
        assertEquals("newFirstName", updatedUser.get().getFirstName());
        assertEquals("newLastName", updatedUser.get().getLastName());
        assertEquals("newEmail@test.com", updatedUser.get().getEmail());
        assertEquals("newPassword", updatedUser.get().getPassword());
    }

    @Test
    void updateUserThrowsExceptionWhenUserNotFound() {
        Long nonExistingUserId = 999L;
        UpdateUserRequest updateRequest = new UpdateUserRequest("newUsername", "newFirstName", "newLastName", "newEmail@test.com", "newPassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(nonExistingUserId, updateRequest);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void addCommentSuccessfully() {
        AddCommentRequest request = new AddCommentRequest(testItem.getItemId(), "This is a test comment.");

        CommentResponse response = userService.addComment(testUser.getUserId(), request);

        assertNotNull(response);
        assertEquals("This is a test comment.", response.getContent());
        assertEquals(testUser.getUserId(), response.getAuthorId());
        assertEquals(testItem.getItemId(), response.getItem().getItemId());

        Optional<Comment> savedComment = commentRepository.findById(response.getCommentId());
        assertTrue(savedComment.isPresent());
        assertEquals("This is a test comment.", savedComment.get().getContent());
        assertEquals(testUser.getUserId(), savedComment.get().getAuthor().getUserId());
        assertEquals(testItem.getItemId(), savedComment.get().getItem().getItemId());
    }

    @Test
    void addCommentThrowsExceptionWhenUserNotInGroup() {
        String anotherUniqueUsername = "another-username-" + UUID.randomUUID();
        User anotherUser = new User(anotherUniqueUsername, "firstName", "lastName", "anotheremail@test.com", "password", LocalDateTime.now(), LocalDateTime.now());
        anotherUser = userRepository.save(anotherUser);

        AddCommentRequest request = new AddCommentRequest(testItem.getItemId(), "This is a test comment.");

        User finalAnotherUser = anotherUser;
        assertThrows(UserNotInGroupException.class, () -> {
            userService.addComment(finalAnotherUser.getUserId(), request);
        });
    }

    @Test
    void addCommentThrowsExceptionWhenItemNotFound() {
        AddCommentRequest request = new AddCommentRequest(999L, "This is a test comment.");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.addComment(testUser.getUserId(), request);
        });

        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    void addCommentThrowsExceptionWhenUserNotFound() {
        AddCommentRequest request = new AddCommentRequest(testItem.getItemId(), "This is a test comment.");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.addComment(999L, request);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void editCommentSuccessfully() {
        EditCommentRequest request = new EditCommentRequest(testUser.getUserId(), "Updated comment content");

        CommentResponse response = userService.editComment(testComment.getCommentId(), request);

        assertNotNull(response);
        assertEquals("Updated comment content", response.getContent());
        assertEquals(testComment.getCommentId(), response.getCommentId());

        Optional<Comment> updatedComment = commentRepository.findById(testComment.getCommentId());
        assertTrue(updatedComment.isPresent());
        assertEquals("Updated comment content", updatedComment.get().getContent());
        assertEquals(testUser.getUserId(), updatedComment.get().getAuthor().getUserId());
    }

    @Test
    void editCommentThrowsExceptionWhenCommentNotFound() {
        EditCommentRequest request = new EditCommentRequest(testUser.getUserId(), "Updated comment content");

        Long nonExistingCommentId = 999L;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.editComment(nonExistingCommentId, request);
        });

        assertEquals("Comment not found", exception.getMessage());
    }

    @Test
    void editCommentThrowsExceptionWhenUserIsNotAuthor() {
        EditCommentRequest request = new EditCommentRequest(anotherUser.getUserId(), "Trying to edit another user's comment");

        NotUsersCommentException exception = assertThrows(NotUsersCommentException.class, () -> {
            userService.editComment(testComment.getCommentId(), request);
        });

        assertEquals("User with ID: " + anotherUser.getUserId() + " is not author of comment with ID: " + testComment.getCommentId(), exception.getMessage());
    }

    @Test
    void deleteUserTest() {
        Long userId = testUser.getUserId();

        Optional<User> userBeforeDeletion = userRepository.findById(userId);
        assertTrue(userBeforeDeletion.isPresent());

        userService.deleteUser(userId);

        Optional<User> userAfterDeletion = userRepository.findById(userId);
        assertFalse(userAfterDeletion.isPresent());
    }
}
