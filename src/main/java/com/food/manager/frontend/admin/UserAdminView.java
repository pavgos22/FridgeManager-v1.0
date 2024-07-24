package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.user.AddCommentRequest;
import com.food.manager.backend.dto.request.user.CreateUserRequest;
import com.food.manager.backend.dto.request.user.EditCommentRequest;
import com.food.manager.backend.dto.response.CommentResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

    private final TextField username = new TextField("Username");
    private final TextField firstNameField = new TextField("First Name");
    private final TextField lastNameField = new TextField("Last Name");
    private final TextField emailField = new TextField("Email");
    private final TextField passwordField = new TextField("Password");
    private final Button saveButton = new Button("Save");

    private final TextField userIdField = new TextField("User ID to delete");
    private final Button deleteButton = new Button("Delete");

    private final TextField addCommentUserId = new TextField("User ID");
    private final TextField addCommentItemId = new TextField("Item ID");
    private final TextField addCommentContent = new TextField("Content");
    private final Button addCommentButton = new Button("Add Comment");

    private final TextField editCommentId = new TextField("Comment ID");
    private final TextField editCommentContent = new TextField("Content");
    private final Button editCommentButton = new Button("Edit Comment");

    public UserAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("userId", "username", "firstName", "lastName", "email", "createdAt", "updatedAt");
        add(grid);
    }

    private void setupForm() {
        saveButton.addClickListener(e -> createUser());
        deleteButton.addClickListener(e -> deleteUser());
        addCommentButton.addClickListener(e -> addComment());
        editCommentButton.addClickListener(e -> editComment());

        VerticalLayout createUserForm = new VerticalLayout(username, firstNameField, lastNameField, emailField, passwordField, saveButton);
        createUserForm.setSpacing(true);
        createUserForm.setPadding(true);

        VerticalLayout deleteUserForm = new VerticalLayout(userIdField, deleteButton);
        deleteUserForm.setSpacing(true);
        deleteUserForm.setPadding(true);

        VerticalLayout addCommentForm = new VerticalLayout(addCommentUserId, addCommentItemId, addCommentContent, addCommentButton);
        addCommentForm.setSpacing(true);
        addCommentForm.setPadding(true);

        VerticalLayout editCommentForm = new VerticalLayout(editCommentId, editCommentContent, editCommentButton);
        editCommentForm.setSpacing(true);
        editCommentForm.setPadding(true);

        HorizontalLayout formsLayout = new HorizontalLayout(createUserForm, deleteUserForm, addCommentForm, editCommentForm);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<UserResponse> users = response.getBody();
        grid.setItems(users);
    }

    private void createUser() {
        CreateUserRequest request = new CreateUserRequest(
                username.getValue(),
                firstNameField.getValue(),
                lastNameField.getValue(),
                emailField.getValue(),
                passwordField.getValue()
        );
        restTemplate.postForObject(BASE_URL, request, UserResponse.class);
        loadData();
    }

    private void deleteUser() {
        String userId = userIdField.getValue();
        restTemplate.delete(BASE_URL + "/" + userId);
        loadData();
    }

    private void addComment() {
        AddCommentRequest request = new AddCommentRequest(
                Long.parseLong(addCommentUserId.getValue()),
                Long.parseLong(addCommentItemId.getValue()),
                addCommentContent.getValue()
        );
        restTemplate.postForObject(BASE_URL + "/addComment", request, CommentResponse.class);
    }

    private void editComment() {
        EditCommentRequest request = new EditCommentRequest(
                Long.parseLong(editCommentId.getValue()),
                editCommentContent.getValue()
        );
        restTemplate.patchForObject(BASE_URL + "/editComment", request, CommentResponse.class);
    }
}
