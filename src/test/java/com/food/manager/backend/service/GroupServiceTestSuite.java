package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.group.AddUserRequest;
import com.food.manager.backend.dto.request.group.UpdateGroupRequest;
import com.food.manager.backend.dto.request.user.DeleteUserRequest;
import com.food.manager.backend.dto.response.GroupResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.entity.*;
import com.food.manager.backend.exception.GroupNotFoundException;
import com.food.manager.backend.exception.UserAlreadyExistsInGroupException;
import com.food.manager.backend.exception.UserNotFoundException;
import com.food.manager.backend.mapper.GroupMapper;
import com.food.manager.backend.mapper.ShoppingListItemMapper;
import com.food.manager.backend.mapper.UserMapper;
import com.food.manager.backend.repository.GroupRepository;
import com.food.manager.backend.repository.ShoppingListItemRepository;
import com.food.manager.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTestSuite {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShoppingListItemRepository shoppingListItemRepository;

    @Mock
    private ShoppingListItemMapper shoppingListItemMapper;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private FridgeService fridgeService;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returnsAllGroups() {
        Group group1 = new Group("Group 1");
        Group group2 = new Group("Group 2");
        List<Group> groups = Arrays.asList(group1, group2);
        GroupResponse response1 = new GroupResponse();
        GroupResponse response2 = new GroupResponse();

        when(groupRepository.findAll()).thenReturn(groups);
        when(groupMapper.mapToGroupsList(groups)).thenReturn(Arrays.asList(response1, response2));

        List<GroupResponse> result = groupService.getAllGroups();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
        verify(groupRepository, times(1)).findAll();
        verify(groupMapper, times(1)).mapToGroupsList(groups);
    }

    @Test
    void returnsAllUsersInGroup() {
        Long groupId = 1L;
        Group group = new Group("Group 1");
        User user1 = new User("user1", "John", "Doe", "john@example.com", "password", null, null);
        User user2 = new User("user2", "Jane", "Doe", "jane@example.com", "password", null, null);
        group.getUsers().add(user1);
        group.getUsers().add(user2);
        UserResponse response1 = new UserResponse();
        UserResponse response2 = new UserResponse();

        when(groupRepository.findGroupWithUsers(groupId)).thenReturn(group);
        when(userMapper.mapToUsersList(group.getUsers())).thenReturn(Arrays.asList(response1, response2));

        List<UserResponse> result = groupService.getAllUsersInGroup(groupId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
        verify(groupRepository, times(1)).findGroupWithUsers(groupId);
        verify(userMapper, times(1)).mapToUsersList(group.getUsers());
    }

    @Test
    void returnsGroupWhenExists() {
        Long groupId = 1L;
        Group group = new Group("Group 1");
        GroupResponse response = new GroupResponse();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMapper.toGroupResponse(group)).thenReturn(response);

        Optional<GroupResponse> result = groupService.getGroup(groupId);

        assertTrue(result.isPresent());
        assertEquals(response, result.get());
        verify(groupRepository, times(1)).findById(groupId);
        verify(groupMapper, times(1)).toGroupResponse(group);
    }

    @Test
    void returnsEmptyWhenGroupDoesNotExist() {
        Long groupId = 1L;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        Optional<GroupResponse> result = groupService.getGroup(groupId);

        assertFalse(result.isPresent());
        verify(groupRepository, times(1)).findById(groupId);
        verify(groupMapper, times(0)).toGroupResponse(any(Group.class));
    }

    @Test
    void updateGroupSuccessfully() {
        Long groupId = 1L;
        UpdateGroupRequest updateGroupRequest = new UpdateGroupRequest("Updated Group");
        Group group = new Group("Old Group", LocalDateTime.now(), LocalDateTime.now());
        group.setGroupId(groupId);

        GroupResponse groupResponse = new GroupResponse();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepository.save(group)).thenReturn(group);
        when(groupMapper.toGroupResponse(group)).thenReturn(groupResponse);

        GroupResponse result = groupService.updateGroup(groupId, updateGroupRequest);

        assertNotNull(result);
        assertEquals(groupResponse, result);
        assertEquals("Updated Group", group.getGroupName());
        verify(groupRepository, times(1)).findById(groupId);
        verify(groupRepository, times(1)).save(group);
        verify(groupMapper, times(1)).toGroupResponse(group);
    }

    @Test
    void updateGroupThrowsExceptionWhenGroupNotFound() {
        Long groupId = 1L;
        UpdateGroupRequest updateGroupRequest = new UpdateGroupRequest("Updated Group");

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            groupService.updateGroup(groupId, updateGroupRequest);
        });

        assertEquals("Group with ID: " + groupId + " not found", exception.getMessage());
        verify(groupRepository, times(1)).findById(groupId);
        verify(groupRepository, times(0)).save(any(Group.class));
        verify(groupMapper, times(0)).toGroupResponse(any(Group.class));
    }

    @Test
    void addUserSuccessfully() {
        Long groupId = 1L;
        Long userId = 1L;
        AddUserRequest addUserRequest = new AddUserRequest(groupId, userId);

        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        group.setGroupId(groupId);

        User user = new User("user1", "John", "Doe", "john@example.com", "password", LocalDateTime.now(), LocalDateTime.now());
        user.setUserId(userId);

        GroupResponse groupResponse = new GroupResponse();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupMapper.toGroupResponse(group)).thenReturn(groupResponse);

        GroupResponse result = groupService.addUser(addUserRequest);

        assertNotNull(result);
        assertEquals(groupResponse, result);
        assertTrue(group.getUsers().contains(user));
        assertTrue(user.getGroups().contains(group));

        verify(groupRepository, times(1)).findById(groupId);
        verify(userRepository, times(1)).findById(userId);
        verify(groupRepository, times(1)).save(group);
        verify(userRepository, times(1)).save(user);
        verify(groupMapper, times(1)).toGroupResponse(group);
    }

    @Test
    void addUserThrowsGroupNotFoundException() {
        Long groupId = 1L;
        Long userId = 1L;
        AddUserRequest addUserRequest = new AddUserRequest(groupId, userId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> {
            groupService.addUser(addUserRequest);
        });

        assertEquals("Group with ID: " + groupId + " not found", exception.getMessage());

        verify(groupRepository, times(1)).findById(groupId);
        verify(userRepository, times(0)).findById(anyLong());
        verify(groupRepository, times(0)).save(any(Group.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(groupMapper, times(0)).toGroupResponse(any(Group.class));
    }

    @Test
    void addUserThrowsUserNotFoundException() {
        Long groupId = 1L;
        Long userId = 1L;
        AddUserRequest addUserRequest = new AddUserRequest(groupId, userId);

        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        group.setGroupId(groupId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            groupService.addUser(addUserRequest);
        });

        assertEquals("User with ID: " + userId + " not found", exception.getMessage());

        verify(groupRepository, times(1)).findById(groupId);
        verify(userRepository, times(1)).findById(userId);
        verify(groupRepository, times(0)).save(any(Group.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(groupMapper, times(0)).toGroupResponse(any(Group.class));
    }

    @Test
    void addUserThrowsUserAlreadyInGroupException() {
        Long groupId = 1L;
        Long userId = 1L;
        AddUserRequest addUserRequest = new AddUserRequest(groupId, userId);

        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        group.setGroupId(groupId);

        User user = new User("user1", "John", "Doe", "john@example.com", "password", LocalDateTime.now(), LocalDateTime.now());
        user.setUserId(userId);
        group.getUsers().add(user);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserAlreadyExistsInGroupException exception = assertThrows(UserAlreadyExistsInGroupException.class, () -> {
            groupService.addUser(addUserRequest);
        });

        assertEquals("User with ID: " + userId + " already exists in group with ID: " + groupId, exception.getMessage());


        verify(groupRepository, times(1)).findById(groupId);
        verify(userRepository, times(1)).findById(userId);
        verify(groupRepository, times(0)).save(any(Group.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(groupMapper, times(0)).toGroupResponse(any(Group.class));
    }

    @Test
    void deleteUserSuccessfully() {
        Long groupId = 1L;
        Long userId = 1L;
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(groupId, userId);

        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        group.setGroupId(groupId);

        User user = new User("user1", "John", "Doe", "john@example.com", "password", LocalDateTime.now(), LocalDateTime.now());
        user.setUserId(userId);

        group.getUsers().add(user);
        user.getGroups().add(group);

        GroupResponse groupResponse = new GroupResponse();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupMapper.toGroupResponse(group)).thenReturn(groupResponse);

        GroupResponse result = groupService.deleteUser(deleteUserRequest);

        assertNotNull(result);
        assertEquals(groupResponse, result);
        assertFalse(group.getUsers().contains(user));
        assertFalse(user.getGroups().contains(group));

        verify(groupRepository, times(1)).findById(groupId);
        verify(userRepository, times(1)).findById(userId);
        verify(groupRepository, times(1)).save(group);
        verify(userRepository, times(1)).save(user);
        verify(groupMapper, times(1)).toGroupResponse(group);
    }

    @Test
    void deleteUserThrowsGroupNotFoundException() {
        Long groupId = 1L;
        Long userId = 1L;
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(groupId, userId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> {
            groupService.deleteUser(deleteUserRequest);
        });

        assertEquals("Group with ID: " + groupId + " not found", exception.getMessage());

        verify(groupRepository, times(1)).findById(groupId);
        verify(userRepository, times(0)).findById(anyLong());
        verify(groupRepository, times(0)).save(any(Group.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(groupMapper, times(0)).toGroupResponse(any(Group.class));
    }

    @Test
    void deleteUserThrowsUserNotFoundException() {
        Long groupId = 1L;
        Long userId = 1L;
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest(groupId, userId);

        Group group = new Group("Test Group", LocalDateTime.now(), LocalDateTime.now());
        group.setGroupId(groupId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            groupService.deleteUser(deleteUserRequest);
        });

        assertEquals("User with ID: " + userId + " not found", exception.getMessage());

        verify(groupRepository, times(1)).findById(groupId);
        verify(userRepository, times(1)).findById(userId);
        verify(groupRepository, times(0)).save(any(Group.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(groupMapper, times(0)).toGroupResponse(any(Group.class));
    }


    @Test
    void getShoppingListItemsByGroupSuccessfully() {
        Long groupId = 1L;

        Group group = new Group("Test Group");
        group.setGroupId(groupId);

        Product product1 = new Product("Milk");
        Product product2 = new Product("Bread");

        ShoppingListItem item1 = new ShoppingListItem(product1, null, 1, false, group);
        ShoppingListItem item2 = new ShoppingListItem(product2, null, 2, false, group);

        List<ShoppingListItem> items = Arrays.asList(item1, item2);
        ShoppingListItemResponse response1 = new ShoppingListItemResponse();
        ShoppingListItemResponse response2 = new ShoppingListItemResponse();

        when(shoppingListItemRepository.findByGroupId(groupId)).thenReturn(items);
        when(shoppingListItemMapper.mapToShoppingListItemList(items)).thenReturn(Arrays.asList(response1, response2));

        List<ShoppingListItemResponse> result = groupService.getShoppingListItemsByGroup(groupId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(shoppingListItemRepository, times(1)).findByGroupId(groupId);
        verify(shoppingListItemMapper, times(1)).mapToShoppingListItemList(items);
    }

    @Test
    void getShoppingListItemsByGroupReturnsEmptyListWhenNoItemsFound() {
        Long groupId = 1L;

        when(shoppingListItemRepository.findByGroupId(groupId)).thenReturn(Collections.emptyList());
        when(shoppingListItemMapper.mapToShoppingListItemList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ShoppingListItemResponse> result = groupService.getShoppingListItemsByGroup(groupId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(shoppingListItemRepository, times(1)).findByGroupId(groupId);
        verify(shoppingListItemMapper, times(1)).mapToShoppingListItemList(Collections.emptyList());
    }

    @Test
    void deleteGroupSuccessfully() {
        Long groupId = 1L;

        doNothing().when(groupRepository).deleteById(groupId);

        groupService.deleteGroup(groupId);

        verify(groupRepository, times(1)).deleteById(groupId);
    }
}
