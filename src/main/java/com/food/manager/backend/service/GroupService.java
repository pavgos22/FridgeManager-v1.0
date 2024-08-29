package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.group.*;
import com.food.manager.backend.dto.request.item.CreateItemRequest;
import com.food.manager.backend.dto.request.user.DeleteUserRequest;
import com.food.manager.backend.dto.response.GroupResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.entity.*;
import com.food.manager.backend.exception.*;
import com.food.manager.backend.mapper.GroupMapper;
import com.food.manager.backend.mapper.ShoppingListItemMapper;
import com.food.manager.backend.mapper.UserMapper;
import com.food.manager.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShoppingListItemMapper shoppingListItemMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private FridgeService fridgeService;


    public List<GroupResponse> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groupMapper.mapToGroupsList(groups);
    }

    public List<UserResponse> getAllUsersInGroup(Long groupId) {
        Group group = groupRepository.findGroupWithUsers(groupId);
        List<User> users = group.getUsers();
        return userMapper.mapToUsersList(users);
    }

    public Optional<GroupResponse> getGroup(Long id) {
        return groupRepository.findById(id).map(groupMapper::toGroupResponse);
    }

    public Map<ShoppingListItemResponse, Map<String, String>> getItemsWithComments(Long groupId) {
        List<ShoppingListItem> items = shoppingListItemRepository.findByGroupId(groupId);

        Map<ShoppingListItemResponse, Map<String, String>> itemsWithComments = new HashMap<>();

        for (ShoppingListItem item : items) {
            Map<String, String> commentsMap = item.getComments().stream()
                    .collect(Collectors.toMap(comment -> comment.getAuthor().getUsername(), Comment::getContent));

            if (!commentsMap.isEmpty()) {
                ShoppingListItemResponse itemResponse = shoppingListItemMapper.toShoppingListItemResponse(item);
                itemsWithComments.put(itemResponse, commentsMap);
            }
        }
        return itemsWithComments;
    }


    public GroupResponse createGroup(CreateGroupRequest createGroupRequest) {
        Group group = new Group(createGroupRequest.groupName(), LocalDateTime.now(), LocalDateTime.now());
        group = groupRepository.save(group);
        Fridge fridge = fridgeService.createFridge(group.getGroupId());
        group.setGroupFridge(fridge);
        return groupMapper.toGroupResponse(groupRepository.save(group));
    }

    public GroupResponse updateGroup(Long id, UpdateGroupRequest updateGroupRequest) {
        Optional<Group> optionalGroup = groupRepository.findById(id);

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setGroupName(updateGroupRequest.groupName());
            group.setUpdatedAt(LocalDateTime.now());
            return groupMapper.toGroupResponse(groupRepository.save(group));
        } else
            throw new GroupNotFoundException(id);
    }


    public GroupResponse addUser(AddUserRequest addUserRequest) {
        Group group = groupRepository.findById(addUserRequest.groupId()).orElseThrow(() -> new GroupNotFoundException(addUserRequest.groupId()));

        User user = userRepository.findById(addUserRequest.userId()).orElseThrow(() -> new UserNotFoundException(addUserRequest.userId()));

        if (group.getUsers().contains(user))
            throw new UserAlreadyExistsInGroupException(addUserRequest.userId(), addUserRequest.groupId());


        group.getUsers().add(user);
        user.getGroups().add(group);

        groupRepository.save(group);
        userRepository.save(user);

        return groupMapper.toGroupResponse(group);
    }


    public GroupResponse deleteUser(DeleteUserRequest deleteUserRequest) {
        Group group = groupRepository.findById(deleteUserRequest.groupId())
                .orElseThrow(() -> new GroupNotFoundException(deleteUserRequest.groupId()));

        User user = userRepository.findById(deleteUserRequest.userId())
                .orElseThrow(() -> new UserNotFoundException(deleteUserRequest.userId()));

        group.getUsers().remove(user);
        user.getGroups().remove(group);

        groupRepository.save(group);
        userRepository.save(user);

        return groupMapper.toGroupResponse(group);
    }

    public GroupResponse addItemToGroup(Long groupId, CreateItemRequest createItemRequest) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        Product product = productRepository.findById(createItemRequest.productId()).orElseThrow(() -> new ProductNotFoundInProductsException(createItemRequest.productId()));

        Optional<ShoppingListItem> existingItemOptional = group.getShoppingListItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(createItemRequest.productId()))
                .findFirst();

        if (existingItemOptional.isPresent()) {
            ShoppingListItem existingItem = existingItemOptional.get();
            existingItem.setQuantity(existingItem.getQuantity() + createItemRequest.quantity());
            shoppingListItemRepository.save(existingItem);
        } else {
            ShoppingListItem newItem = new ShoppingListItem(product, createItemRequest.quantityType(), createItemRequest.quantity(), false, group);
            shoppingListItemRepository.save(newItem);
            group.getShoppingListItems().add(newItem);
        }

        groupRepository.save(group);
        return groupMapper.toGroupResponse(group);
    }


    public GroupResponse removeItemFromGroup(Long groupId, RemoveItemFromGroupRequest removeItemFromGroupRequest) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        ShoppingListItem item = shoppingListItemRepository.findById(removeItemFromGroupRequest.itemId())
                .orElseThrow(() -> new ShoppingListItemNotFoundException(removeItemFromGroupRequest.itemId()));

        if (item.getQuantity() > removeItemFromGroupRequest.quantity()) {
            item.setQuantity(item.getQuantity() - removeItemFromGroupRequest.quantity());
            shoppingListItemRepository.save(item);
        } else {
            group.getShoppingListItems().remove(item);
            shoppingListItemRepository.delete(item);
        }

        groupRepository.save(group);
        return groupMapper.toGroupResponse(group);
    }

    public List<ShoppingListItemResponse> getShoppingListItemsByGroup(Long groupId) {
        List<ShoppingListItem> items = shoppingListItemRepository.findByGroupId(groupId);
        return shoppingListItemMapper.mapToShoppingListItemList(items);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }


}
