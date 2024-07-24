package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.fridge.AddProductRequest;
import com.food.manager.backend.dto.request.fridge.RemoveProductFromFridgeRequest;
import com.food.manager.backend.dto.response.FridgeResponse;
import com.food.manager.backend.enums.QuantityType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("admin/fridges")
public class FridgeAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/fridges";
    private final Grid<FridgeResponse> grid = new Grid<>(FridgeResponse.class);

    private final TextField addFridgeIdField = new TextField("Fridge ID");
    private final TextField addProductIdField = new TextField("Product ID");
    private final ComboBox<QuantityType> addQuantityTypeField = new ComboBox<>("Quantity Type");
    private final TextField addQuantityField = new TextField("Quantity");
    private final Button addProductButton = new Button("Add Product");

    private final TextField removeFridgeIdField = new TextField("Fridge ID");
    private final TextField removeFridgeProductIdField = new TextField("Fridge Product ID");
    private final TextField removeQuantityField = new TextField("Quantity");
    private final Button removeProductButton = new Button("Remove Product");

    private final TextField executeFridgeIdField = new TextField("Fridge ID");
    private final TextField executeRecipeIdField = new TextField("Recipe ID");
    private final Button executeRecipeButton = new Button("Execute Recipe");

    public FridgeAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("fridgeId", "group", "fridgeProducts");
        add(grid);
    }

    private void setupForm() {
        addProductButton.addClickListener(e -> addProductToFridge());
        removeProductButton.addClickListener(e -> removeProductFromFridge());
        executeRecipeButton.addClickListener(e -> executeRecipe());

        addQuantityTypeField.setItems(QuantityType.values());
        VerticalLayout addProductForm = new VerticalLayout(addFridgeIdField, addProductIdField, addQuantityTypeField, addQuantityField, addProductButton);
        addProductForm.setSpacing(true);
        addProductForm.setPadding(true);

        VerticalLayout removeProductForm = new VerticalLayout(removeFridgeIdField, removeFridgeProductIdField, removeQuantityField, removeProductButton);
        removeProductForm.setSpacing(true);
        removeProductForm.setPadding(true);

        VerticalLayout executeRecipeForm = new VerticalLayout(executeFridgeIdField, executeRecipeIdField, executeRecipeButton);
        executeRecipeForm.setSpacing(true);
        executeRecipeForm.setPadding(true);

        HorizontalLayout formsLayout = new HorizontalLayout(addProductForm, removeProductForm, executeRecipeForm);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<FridgeResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<FridgeResponse> fridges = response.getBody();
        grid.setItems(fridges);
    }

    private void addProductToFridge() {
        Long fridgeId = Long.parseLong(addFridgeIdField.getValue());
        Long productId = Long.parseLong(addProductIdField.getValue());
        QuantityType quantityType = addQuantityTypeField.getValue();
        int quantity = Integer.parseInt(addQuantityField.getValue());

        AddProductRequest request = new AddProductRequest(fridgeId, productId, quantityType, quantity);
        restTemplate.put(BASE_URL + "/addProduct", request, FridgeResponse.class);
        loadData();
    }

    private void removeProductFromFridge() {
        Long fridgeId = Long.parseLong(removeFridgeIdField.getValue());
        Long fridgeProductId = Long.parseLong(removeFridgeProductIdField.getValue());
        int quantity = Integer.parseInt(removeQuantityField.getValue());

        RemoveProductFromFridgeRequest request = new RemoveProductFromFridgeRequest(fridgeId, fridgeProductId, quantity);
        restTemplate.put(BASE_URL + "/removeProduct", request, FridgeResponse.class);
        loadData();
    }

    private void executeRecipe() {
        Long fridgeId = Long.parseLong(executeFridgeIdField.getValue());
        Long recipeId = Long.parseLong(executeRecipeIdField.getValue());

        restTemplate.put(BASE_URL + "/" + fridgeId + "/executeRecipe/" + recipeId, null, FridgeResponse.class);
        loadData();
    }
}
