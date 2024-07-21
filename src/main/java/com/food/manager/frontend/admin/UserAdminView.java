package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.UserResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("admin/users")
public class UserAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/users";
    private final Grid<UserResponse> grid = new Grid<>(UserResponse.class);

    public UserAdminView() {
        setupGrid();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("userId", "username", "firstName", "lastName", "email", "createdAt", "updatedAt");
        add(grid);
    }

    private void loadData() {
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                BASE_URL,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<UserResponse> users = response.getBody();
        grid.setItems(users);
    }
}
