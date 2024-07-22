package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.GroupResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("admin/groups")
public class GroupAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/groups";
    private final Grid<GroupResponse> grid = new Grid<>(GroupResponse.class);

    public GroupAdminView() {
        setupGrid();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("groupId", "groupName", "createdAt", "updatedAt", "fridgeId");
        add(grid);
    }

    private void loadData() {
        ResponseEntity<List<GroupResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<GroupResponse> groups = response.getBody();
        grid.setItems(groups);
    }
}