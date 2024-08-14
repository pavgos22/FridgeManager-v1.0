package com.food.manager.backend.mapper;

import com.food.manager.backend.dto.response.GroupResponse;
import com.food.manager.backend.entity.Group;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {

    private final UserMapper userMapper;
    private final ShoppingListItemMapper itemMapper;

    public GroupMapper(UserMapper userMapper, ShoppingListItemMapper itemMapper) {
        this.userMapper = userMapper;
        this.itemMapper = itemMapper;
    }

    public GroupResponse toGroupResponse(Group group) {
        if (group == null) {
            return null;
        }
        return new GroupResponse(
                group.getGroupId(),
                group.getGroupName(),
                group.getCreatedAt(),
                group.getUpdatedAt(),
                userMapper.mapToUsersList(group.getUsers()),
                (group.getGroupFridge() != null && group.getGroupFridge().getFridgeId() != null) ? group.getGroupFridge().getFridgeId() : null,
                itemMapper.mapToShoppingListItemList(group.getShoppingListItems())
        );
    }

    public List<GroupResponse> mapToGroupsList(List<Group> groups) {
        return groups.stream()
                .map(this::toGroupResponse)
                .toList();
    }
}