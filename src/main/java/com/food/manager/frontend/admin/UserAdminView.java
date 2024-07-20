package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.user.CreateUserRequest;
import com.food.manager.backend.dto.request.user.UpdateUserRequest;
import com.food.manager.backend.dto.response.UserResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("admin/users")
public class UserAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Grid<UserResponse> grid = new Grid<>(UserResponse.class);
    private final TextField username = new TextField("Username");
    private final TextField firstNameField = new TextField("First name");
    private final TextField lastNameField = new TextField("Last name");
    private final TextField emailField = new TextField("Email");
    private final TextField passwordField = new TextField("Password");
    private final Button createButton = new Button("Create User");
    private final Button updateButton = new Button("Update User");
    private final Button deleteButton = new Button("Delete User");

    public UserAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("id", "name", "email");
        add(grid);
    }

    private void setupForm() {
        createButton.addClickListener(e -> createUser());
        updateButton.addClickListener(e -> updateUser());
        deleteButton.addClickListener(e -> deleteUser());

        add(username, emailField, createButton, updateButton, deleteButton);
    }

    private void loadData() {
        List<UserResponse> users = restTemplate.getForObject("/v1/users", List.class);
        grid.setItems(users);
    }

    private void createUser() {
        CreateUserRequest request = new CreateUserRequest(username.getValue(), firstNameField.getValue(), lastNameField.getValue(), emailField.getValue(), passwordField.getValue());
        restTemplate.postForObject("/v1/users", request, UserResponse.class);
        loadData();
    }

    private void updateUser() {
        UserResponse selectedUser = grid.asSingleSelect().getValue();
        if (selectedUser != null) {
            UpdateUserRequest request = new UpdateUserRequest(selectedUser.userId(), username.getValue(), emailField.getValue());
            restTemplate.put("/v1/users/" + selectedUser.userId(), request);
            loadData();
        }
    }

    private void deleteUser() {
        UserResponse selectedUser = grid.asSingleSelect().getValue();
        if (selectedUser != null) {
            restTemplate.delete("/v1/users/" + selectedUser.userId());
            loadData();
        }
    }
}
