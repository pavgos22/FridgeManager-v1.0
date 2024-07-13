package com.food.manager.service;

import com.food.manager.dto.request.group.*;
import com.food.manager.dto.request.item.CreateItemRequest;
import com.food.manager.dto.request.user.DeleteUserRequest;
import com.food.manager.dto.response.GroupResponse;
import com.food.manager.entity.Group;
import com.food.manager.entity.Product;
import com.food.manager.entity.ShoppingListItem;
import com.food.manager.entity.User;
import com.food.manager.mapper.GroupMapper;
import com.food.manager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private CommentRepository commentRepository;


    public List<GroupResponse> getAllUsers() {
        List<Group> groups = groupRepository.findAll();
        return groupMapper.mapToGroupsList(groups);
    }

    public Optional<GroupResponse> getGroup(Long id) {
        return groupRepository.findById(id).map(groupMapper::toGroupResponse);
    }

    public GroupResponse createGroup(CreateGroupRequest createGroupRequest) {
        Group group = new Group(createGroupRequest.groupName(), LocalDateTime.now(), LocalDateTime.now());
        return groupMapper.toGroupResponse(groupRepository.save(group));
    }

    public GroupResponse updateGroup(UpdateGroupRequest updateGroupRequest) {
        Optional<Group> optionalGroup = groupRepository.findById(updateGroupRequest.groupId());

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setGroupName(updateGroupRequest.groupName());
            group.setUpdatedAt(LocalDateTime.now());
            return groupMapper.toGroupResponse(groupRepository.save(group));
        } else
            throw new RuntimeException("Group not found with id: " + updateGroupRequest.groupId());
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
        Optional<Group> groupOptional = groupRepository.findById(deleteUserRequest.userId());
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

    public ShoppingListItem createItem(CreateItemRequest createItemRequest) {
        return new ShoppingListItem(
                createItemRequest.product(),
                createItemRequest.quantityType(),
                createItemRequest.quantity(),
                createItemRequest.checked(),
                createItemRequest.group()
        );
    }

    public GroupResponse addItemToGroup(CreateItemRequest createItemRequest, AddItemToGroupRequest addItemToGroupRequest) {
        Optional<Group> groupOptional = groupRepository.findById(addItemToGroupRequest.groupId());
        if (groupOptional.isEmpty()) {
            throw new RuntimeException("Group not found with id: " + addItemToGroupRequest.groupId());
        }

        Group group = groupOptional.get();

        Optional<Product> productOptional = productRepository.findById(createItemRequest.product().getProductId());
        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product not found with id: " + createItemRequest.product().getProductId());
        }

        Product product = productOptional.get();

        ShoppingListItem item = createItem(createItemRequest);

        item = shoppingListItemRepository.save(item);

        group.getShoppingListItems().add(item);
        group = groupRepository.save(group);

        return groupMapper.toGroupResponse(group);
    }

    public GroupResponse removeItemFromGroup(RemoveItemFromGroupRequest removeItemFromGroupRequest) {
        Optional<Group> groupOptional = groupRepository.findById(removeItemFromGroupRequest.groupId());
        if (groupOptional.isEmpty()) {
            throw new RuntimeException("Group not found with id: " + removeItemFromGroupRequest.groupId());
        }

        Group group = groupOptional.get();

        Optional<ShoppingListItem> itemOptional = shoppingListItemRepository.findById(removeItemFromGroupRequest.itemId());
        if (itemOptional.isEmpty()) {
            throw new RuntimeException("Item not found with id: " + removeItemFromGroupRequest.itemId());
        }

        ShoppingListItem item = itemOptional.get();

        group.getShoppingListItems().remove(item);
        shoppingListItemRepository.delete(item);

        group = groupRepository.save(group);

        return groupMapper.toGroupResponse(group);
    }

    public void deleteGroup(DeleteGroupRequest deleteGroupRequest) {
        groupRepository.deleteById(deleteGroupRequest.id());
    }
}
