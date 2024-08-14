package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.group.AddUserRequest;
import com.food.manager.backend.dto.request.group.CreateGroupRequest;
import com.food.manager.backend.dto.request.group.RemoveItemFromGroupRequest;
import com.food.manager.backend.dto.request.group.UpdateGroupRequest;
import com.food.manager.backend.dto.request.item.CreateItemRequest;
import com.food.manager.backend.dto.request.user.DeleteUserRequest;
import com.food.manager.backend.dto.response.GroupResponse;
import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.food.manager.backend.dto.response.UserResponse;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.frontend.admin.window.ItemWindow;
import com.food.manager.frontend.admin.window.UsersWindow;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

    private final TextField createGroupNameField = new TextField("Create Group Name");
    private final Button createSaveButton = new Button("Save");

    private final TextField updateGroupNameField = new TextField("Update Group Name");
    private final TextField groupIdField = new TextField("Group ID");
    private final Button updateButton = new Button("Update");

    private final TextField addUserGroupIdField = new TextField("Group ID");
    private final TextField addUserIdField = new TextField("User ID to Add");
    private final Button addUserButton = new Button("Add User");

    private final TextField removeUserGroupIdField = new TextField("Group ID");
    private final TextField removeUserIdField = new TextField("User ID to Remove");
    private final Button removeUserButton = new Button("Remove User");

    private final TextField addItemGroupIdField = new TextField("Group ID");
    private final TextField addItemProductIdField = new TextField("Product ID");
    private final ComboBox<QuantityType> addItemQuantityTypeField = new ComboBox<>("Quantity Type");
    private final TextField addItemQuantityField = new TextField("Quantity");
    private final Button addItemButton = new Button("Add Item");

    private final TextField removeItemGroupIdField = new TextField("Group ID");
    private final TextField removeItemIdField = new TextField("Item ID");
    private final TextField removeItemQuantityField = new TextField("Quantity");
    private final Button removeItemButton = new Button("Remove Item");

    private final TextField deleteGroupIdField = new TextField("Group ID");
    private final Button deleteGroupButton = new Button("Delete Group");

    private final TextField groupItemsGroupIdField = new TextField("Group ID");
    private final Button viewItemsButton = new Button("View Group Items");

    private final TextField viewUsersGroupIdField = new TextField("Group ID to View Users");
    private final Button viewUsersButton = new Button("View Users");

    public GroupAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("groupId", "groupName", "createdAt", "updatedAt", "fridgeId");
        add(grid);
    }

    private void setupForm() {
        createSaveButton.addClickListener(e -> createGroup());
        updateButton.addClickListener(e -> updateGroup());
        addUserButton.addClickListener(e -> addUser());
        removeUserButton.addClickListener(e -> removeUser());
        addItemButton.addClickListener(e -> addItem());
        removeItemButton.addClickListener(e -> removeItem());
        deleteGroupButton.addClickListener(e -> deleteGroup());
        viewItemsButton.addClickListener(e -> viewGroupItems());
        viewUsersButton.addClickListener(e -> viewUsers());

        VerticalLayout createGroupForm = new VerticalLayout(createGroupNameField, createSaveButton);
        createGroupForm.setSpacing(true);
        createGroupForm.setPadding(true);

        VerticalLayout updateGroupForm = new VerticalLayout(groupIdField, updateGroupNameField, updateButton);
        updateGroupForm.setSpacing(true);
        updateGroupForm.setPadding(true);

        VerticalLayout addUserForm = new VerticalLayout(addUserGroupIdField, addUserIdField, addUserButton);
        addUserForm.setSpacing(true);
        addUserForm.setPadding(true);

        VerticalLayout removeUserForm = new VerticalLayout(removeUserGroupIdField, removeUserIdField, removeUserButton);
        removeUserForm.setSpacing(true);
        removeUserForm.setPadding(true);

        addItemQuantityTypeField.setItems(QuantityType.values());
        VerticalLayout addItemForm = new VerticalLayout(addItemGroupIdField, addItemProductIdField, addItemQuantityTypeField, addItemQuantityField, addItemButton);
        addItemForm.setSpacing(true);
        addItemForm.setPadding(true);

        VerticalLayout removeItemForm = new VerticalLayout(removeItemGroupIdField, removeItemIdField, removeItemQuantityField, removeItemButton);
        removeItemForm.setSpacing(true);
        removeItemForm.setPadding(true);

        VerticalLayout deleteGroupForm = new VerticalLayout(deleteGroupIdField, deleteGroupButton);
        deleteGroupForm.setSpacing(true);
        deleteGroupForm.setPadding(true);

        VerticalLayout viewItemsForm = new VerticalLayout(groupItemsGroupIdField, viewItemsButton);
        viewItemsForm.setSpacing(true);
        viewItemsForm.setPadding(true);

        VerticalLayout viewUsersForm = new VerticalLayout(viewUsersGroupIdField, viewUsersButton);
        viewUsersForm.setSpacing(true);
        viewUsersForm.setPadding(true);

        HorizontalLayout formsLayout = new HorizontalLayout(
                createGroupForm, updateGroupForm, addUserForm, removeUserForm,
                addItemForm, removeItemForm, deleteGroupForm, viewItemsForm, viewUsersForm);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<GroupResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<GroupResponse> groups = response.getBody();
        grid.setItems(groups);
    }

    private void createGroup() {
        CreateGroupRequest request = new CreateGroupRequest(createGroupNameField.getValue());
        restTemplate.postForObject(BASE_URL, request, GroupResponse.class);
        loadData();
    }

    private void updateGroup() {
        Long groupId = Long.parseLong(groupIdField.getValue());
        UpdateGroupRequest request = new UpdateGroupRequest(updateGroupNameField.getValue());
        restTemplate.put(BASE_URL + "/" + groupId, request, GroupResponse.class);
        loadData();
    }

    private void addUser() {
        Long groupId = Long.parseLong(addUserGroupIdField.getValue());
        Long userId = Long.parseLong(addUserIdField.getValue());
        AddUserRequest request = new AddUserRequest(groupId, userId);
        restTemplate.postForObject(BASE_URL + "/addUser", request, GroupResponse.class);
        loadData();
    }

    private void removeUser() {
        Long groupId = Long.parseLong(removeUserGroupIdField.getValue());
        Long userId = Long.parseLong(removeUserIdField.getValue());
        DeleteUserRequest request = new DeleteUserRequest(groupId, userId);
        restTemplate.delete(BASE_URL + "/removeUser", request, GroupResponse.class);
        loadData();
    }

    private void addItem() {
        Long groupId = Long.parseLong(addItemGroupIdField.getValue());
        Long productId = Long.parseLong(addItemProductIdField.getValue());
        QuantityType quantityType = addItemQuantityTypeField.getValue();
        int quantity = Integer.parseInt(addItemQuantityField.getValue());
        CreateItemRequest request = new CreateItemRequest(productId, quantityType, quantity);
        restTemplate.put(BASE_URL + "/" + groupId + "/addItem", request, GroupResponse.class);
        loadData();
    }

    private void removeItem() {
        Long groupId = Long.parseLong(removeItemGroupIdField.getValue());
        Long itemId = Long.parseLong(removeItemIdField.getValue());
        int quantity = Integer.parseInt(removeItemQuantityField.getValue());
        RemoveItemFromGroupRequest request = new RemoveItemFromGroupRequest(itemId, quantity);
        restTemplate.put(BASE_URL + "/" + groupId + "/removeItem", request, GroupResponse.class);
        loadData();
    }

    private void deleteGroup() {
        long groupId = Long.parseLong(deleteGroupIdField.getValue());
        restTemplate.delete(BASE_URL + "/" + groupId);
        loadData();
    }

    private void viewGroupItems() {
        long groupId = Long.parseLong(groupItemsGroupIdField.getValue());
        ResponseEntity<List<ShoppingListItemResponse>> response = restTemplate.exchange(
                BASE_URL + "/" + groupId + "/items",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<ShoppingListItemResponse> items = response.getBody();

        Grid<ShoppingListItemResponse> itemsGrid = new Grid<>(ShoppingListItemResponse.class);
        itemsGrid.setColumns("itemId", "productId", "groupId", "quantityType", "quantity", "checked");
        itemsGrid.setItems(items);

        new ItemWindow(itemsGrid);
    }

    private void viewUsers() {
        long groupId = Long.parseLong(viewUsersGroupIdField.getValue());
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                BASE_URL + "/" + groupId + "/users",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<UserResponse> users = response.getBody();

        Grid<UserResponse> usersGrid = new Grid<>(UserResponse.class);
        usersGrid.setColumns("userId", "username", "firstName", "lastName", "email");
        usersGrid.setItems(users);

        new UsersWindow(usersGrid);
    }
}