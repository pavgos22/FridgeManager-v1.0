package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.ingredient.CreateIngredientRequest;
import com.food.manager.backend.dto.response.IngredientResponse;
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

@Route("admin/ingredients")
public class IngredientAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/ingredients";
    private final Grid<IngredientResponse> grid = new Grid<>(IngredientResponse.class);

    private final ComboBox<QuantityType> quantityTypeField = new ComboBox<>("Quantity Type");
    private final TextField quantityField = new TextField("Quantity");
    private final ComboBox<Boolean> requiredField = new ComboBox<>("Required");
    private final ComboBox<Boolean> ignoreGroupField = new ComboBox<>("Ignore Group");
    private final TextField productIdField = new TextField("Product ID");
    private final Button createIngredientButton = new Button("Create Ingredient");

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
        createIngredientButton.addClickListener(e -> createIngredient());

        quantityTypeField.setItems(QuantityType.values());
        requiredField.setItems(Boolean.TRUE, Boolean.FALSE);
        ignoreGroupField.setItems(Boolean.TRUE, Boolean.FALSE);

        VerticalLayout createIngredientForm = new VerticalLayout(quantityTypeField, quantityField, requiredField, ignoreGroupField, productIdField, createIngredientButton);
        createIngredientForm.setSpacing(true);
        createIngredientForm.setPadding(true);

        HorizontalLayout formsLayout = new HorizontalLayout(createIngredientForm);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<IngredientResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<IngredientResponse> ingredients = response.getBody();
        grid.setItems(ingredients);
    }

    private void createIngredient() {
        QuantityType quantityType = quantityTypeField.getValue();
        int quantity = Integer.parseInt(quantityField.getValue());
        boolean required = requiredField.getValue();
        boolean ignoreGroup = ignoreGroupField.getValue();
        Long productId = Long.parseLong(productIdField.getValue());

        CreateIngredientRequest request = new CreateIngredientRequest(quantityType, quantity, required, ignoreGroup, productId);
        restTemplate.postForObject(BASE_URL, request, IngredientResponse.class);
        loadData();
    }
}
