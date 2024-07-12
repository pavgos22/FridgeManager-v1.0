package com.food.manager.mapper;

import com.food.manager.dto.response.GroupResponse;
import com.food.manager.entity.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMapper {

    public GroupResponse toGroupResponse(Group group) {
        if (group == null) {
            return null;
        }
        return new GroupResponse(
                group.getGroupId(),
                group.getGroupName(),
                group.getCreatedAt(),
                group.getUpdatedAt(),
                group.getUsers(),
                group.getGroupFridge(),
                group.getShoppingListItems()
        );
    }

    public List<GroupResponse> mapToGroupsList(List<Group> groups) {
        return groups.stream()
                .map(this::toGroupResponse)
                .toList();
    }
}