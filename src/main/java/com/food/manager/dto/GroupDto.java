package com.food.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupDto {
    private Long groupId;
    private String groupName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> userIds;
    private Long groupFridgeId;
    private List<Long> shoppingListItemIds;
}
