package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.group.CreateGroupRequest;
import com.food.manager.backend.dto.request.group.UpdateGroupRequest;
import com.food.manager.backend.dto.response.GroupResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("admin/groups")
public class GroupAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Grid<GroupResponse> grid = new Grid<>(GroupResponse.class);
    private final TextField nameField = new TextField("Group Name");
    private final Button createButton = new Button("Create Group");
    private final Button updateButton = new Button("Update Group");
    private final Button deleteButton = new Button("Delete Group");

    public GroupAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("id", "name");
        add(grid);
    }

    private void setupForm() {
        createButton.addClickListener(e -> createGroup());
        updateButton.addClickListener(e -> updateGroup());
        deleteButton.addClickListener(e -> deleteGroup());

        add(nameField, createButton, updateButton, deleteButton);
    }

    private void loadData() {
        List<GroupResponse> groups = restTemplate.getForObject("/v1/groups", List.class);
        grid.setItems(groups);
    }

    private void createGroup() {
        CreateGroupRequest request = new CreateGroupRequest(nameField.getValue());
        restTemplate.postForObject("/v1/groups", request, GroupResponse.class);
        loadData();
    }

    private void updateGroup() {
        GroupResponse selectedGroup = grid.asSingleSelect().getValue();
        if (selectedGroup != null) {
            UpdateGroupRequest request = new UpdateGroupRequest(selectedGroup.groupId(), nameField.getValue());
            restTemplate.put("/v1/groups/" + selectedGroup.groupId(), request);
            loadData();
        }
    }

    private void deleteGroup() {
        GroupResponse selectedGroup = grid.asSingleSelect().getValue();
        if (selectedGroup != null) {
            restTemplate.delete("/v1/groups/" + selectedGroup.groupId());
            loadData();
        }
    }
}