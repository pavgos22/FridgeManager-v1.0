package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.group.*;
import com.food.manager.backend.dto.request.item.CreateItemRequest;
import com.food.manager.backend.dto.request.user.DeleteUserRequest;
import com.food.manager.backend.dto.response.GroupResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.entity.*;
import com.food.manager.backend.exception.ProductNotFoundInProductsException;
import com.food.manager.backend.mapper.GroupMapper;
import com.food.manager.backend.mapper.ShoppingListItemMapper;
import com.food.manager.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMapper groupMapper;

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


    public List<GroupResponse> getAllUsers() {
        List<Group> groups = groupRepository.findAll();
        return groupMapper.mapToGroupsList(groups);
    }

    public Optional<GroupResponse> getGroup(Long id) {
        return groupRepository.findById(id).map(groupMapper::toGroupResponse);
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
            throw new RuntimeException("Group not found with id: " + id);
    }


    public GroupResponse addUser(AddUserRequest addUserRequest) {
        Optional<Group> groupOptional = groupRepository.findById(addUserRequest.groupId());
        Optional<User> userOptional = userRepository.findById(addUserRequest.userId());

        if (groupOptional.isPresent() && userOptional.isPresent()) {
            Group group = groupOptional.get();
            User user = userOptional.get();

            group.getUsers().add(user);
            user.getGroups().add(group);

            groupRepository.save(group);
            userRepository.save(user);

            return groupMapper.toGroupResponse(group);
        } else {
            throw new RuntimeException("Group or User not found");
        }
    }

    public GroupResponse deleteUser(DeleteUserRequest deleteUserRequest) {
        Optional<Group> groupOptional = groupRepository.findById(deleteUserRequest.groupId());
        Optional<User> userOptional = userRepository.findById(deleteUserRequest.userId());

        if (groupOptional.isPresent() && userOptional.isPresent()) {
            Group group = groupOptional.get();
            User user = userOptional.get();

            group.getUsers().remove(user);
            user.getGroups().remove(group);

            return groupMapper.toGroupResponse(group);
        }
        else
            throw new RuntimeException("Group or User not found");
    }

    public GroupResponse addItemToGroup(CreateItemRequest createItemRequest) {
        Group group = groupRepository.findById(createItemRequest.groupId())
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + createItemRequest.groupId()));

        Product product = productRepository.findById(createItemRequest.productId())
                .orElseThrow(() -> new ProductNotFoundInProductsException("Product with id " + createItemRequest.productId() + " not found"));

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


    public GroupResponse removeItemFromGroup(RemoveItemFromGroupRequest removeItemFromGroupRequest) {
        Group group = groupRepository.findById(removeItemFromGroupRequest.groupId())
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + removeItemFromGroupRequest.groupId()));

        ShoppingListItem item = shoppingListItemRepository.findById(removeItemFromGroupRequest.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + removeItemFromGroupRequest.itemId()));

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


    public void deleteGroup(DeleteGroupRequest deleteGroupRequest) {
        groupRepository.deleteById(deleteGroupRequest.id());
    }
}
