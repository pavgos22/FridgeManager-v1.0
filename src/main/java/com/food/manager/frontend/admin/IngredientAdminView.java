package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.ingredient.CreateIngredientRequest;
import com.food.manager.backend.dto.response.IngredientResponse;
import com.food.manager.backend.enums.QuantityType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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

@Route("admin/ingredients")
public class IngredientAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/ingredients";

    private final ComboBox<QuantityType> quantityTypeField = new ComboBox<>("Quantity Type");
    private final TextField quantityField = new TextField("Quantity");
    private final TextField productIdField = new TextField("Product ID");
    private final Checkbox requiredField = new Checkbox("Required");
    private final Checkbox ignoreGroupField = new Checkbox("Ignore Group");
    private final Button createIngredientButton = new Button("Create Ingredient");

    private final TextField ingredientIdFieldToDelete = new TextField("Ingredient ID to Delete");
    private final Button deleteIngredientButton = new Button("Delete Ingredient");

    private final Grid<IngredientResponse> grid = new Grid<>(IngredientResponse.class);

    public IngredientAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("ingredientId", "quantityType", "quantity", "required", "ignoreGroup", "productId", "recipeId");
        add(grid);
    }

    private void setupForm() {
        quantityTypeField.setItems(QuantityType.values());
        createIngredientButton.addClickListener(e -> createIngredient());
        deleteIngredientButton.addClickListener(e -> deleteIngredient());

        VerticalLayout createFormLayout = new VerticalLayout(
                quantityTypeField,
                quantityField,
                productIdField,
                requiredField,
                ignoreGroupField,
                createIngredientButton
        );

        VerticalLayout deleteFormLayout = new VerticalLayout(
                ingredientIdFieldToDelete,
                deleteIngredientButton
        );

        HorizontalLayout formsLayout = new HorizontalLayout(createFormLayout, deleteFormLayout);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<IngredientResponse>> response = restTemplate.exchange(
                BASE_URL, org.springframework.http.HttpMethod.GET, null,
                new ParameterizedTypeReference<List<IngredientResponse>>() {}
        );
        List<IngredientResponse> ingredients = response.getBody();
        grid.setItems(ingredients);
    }

    private void createIngredient() {
        QuantityType quantityType = quantityTypeField.getValue();
        int quantity = Integer.parseInt(quantityField.getValue());
        Long productId = Long.parseLong(productIdField.getValue());
        boolean required = requiredField.getValue();
        boolean ignoreGroup = ignoreGroupField.getValue();

        CreateIngredientRequest request = new CreateIngredientRequest(
                quantityType, quantity, required, ignoreGroup, productId);

        restTemplate.postForObject(BASE_URL, request, Void.class);
        loadData();
    }

    private void deleteIngredient() {
        Long ingredientId = Long.parseLong(ingredientIdFieldToDelete.getValue());
        restTemplate.delete(BASE_URL + "/" + ingredientId);
        loadData();
    }
}
