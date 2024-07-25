package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.ingredient.CreateIngredientRequest;
import com.food.manager.backend.dto.response.IngredientResponse;
import com.food.manager.backend.dto.response.ProductResponse;
import com.food.manager.backend.enums.QuantityType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Stream;

@Route("admin/ingredients")
public class IngredientAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/ingredients";
    private static final String PRODUCTS_URL = "http://localhost:8080/v1/products/search";
    private final Grid<IngredientResponse> grid = new Grid<>(IngredientResponse.class);

    private final ComboBox<ProductResponse> productComboBox = new ComboBox<>("Product");
    private final ComboBox<QuantityType> quantityTypeComboBox = new ComboBox<>("Quantity Type");
    private final TextField quantityField = new TextField("Quantity");
    private final TextField requiredField = new TextField("Required");
    private final TextField ignoreGroupField = new TextField("Ignore Group");
    private final Button createButton = new Button("Create Ingredient");

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
        quantityTypeComboBox.setItems(QuantityType.values());
        createButton.addClickListener(e -> createIngredient());

        DataProvider<ProductResponse, String> productDataProvider = DataProvider.fromFilteringCallbacks(
                query -> fetchProducts(query.getFilter().orElse("")),
                query -> fetchProductsCount(query.getFilter().orElse(""))
        );
        //productComboBox.setDataProvider(productDataProvider);
        productComboBox.setItemLabelGenerator(ProductResponse::getProductName);

        VerticalLayout createIngredientForm = new VerticalLayout(productComboBox, quantityTypeComboBox, quantityField, requiredField, ignoreGroupField, createButton);
        createIngredientForm.setSpacing(true);
        createIngredientForm.setPadding(true);

        add(createIngredientForm);
    }

    private Stream<ProductResponse> fetchProducts(String filter) {
        ResponseEntity<List<ProductResponse>> response = restTemplate.exchange(
                PRODUCTS_URL + "?name=" + filter,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<ProductResponse> products = response.getBody();
        return products.stream();
    }

    private int fetchProductsCount(String filter) {
        return (int) fetchProducts(filter).count();
    }

    private void loadData() {
        ResponseEntity<List<IngredientResponse>> response = restTemplate.exchange(
                BASE_URL,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<IngredientResponse> ingredients = response.getBody();
        grid.setItems(ingredients);
    }

    private void createIngredient() {
        CreateIngredientRequest request = new CreateIngredientRequest(
                quantityTypeComboBox.getValue(),
                Integer.parseInt(quantityField.getValue()),
                Boolean.parseBoolean(requiredField.getValue()),
                Boolean.parseBoolean(ignoreGroupField.getValue()),
                productComboBox.getValue().getProductId()
        );
        restTemplate.postForObject(BASE_URL, request, IngredientResponse.class);
        loadData();
    }
}
