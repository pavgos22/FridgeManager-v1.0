package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.product.CreateProductRequest;
import com.food.manager.backend.dto.response.ProductResponse;
import com.food.manager.backend.enums.ProductGroup;
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

@Route("admin/products")
public class ProductAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/products";
    private final Grid<ProductResponse> grid = new Grid<>(ProductResponse.class);

    private final TextField productNameField = new TextField("Product Name");
    private final ComboBox<ProductGroup> productGroupField = new ComboBox<>("Product Group");
    private final Button createSaveButton = new Button("Save");

    private final TextField productIdToDeleteField = new TextField("Product ID to Delete");
    private final Button deleteButton = new Button("Delete");

    public ProductAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("productId", "productName", "nutrition", "recipeIds");
        add(grid);
    }

    private void setupForm() {
        createSaveButton.addClickListener(e -> createProduct());
        deleteButton.addClickListener(e -> deleteProduct());

        productGroupField.setItems(ProductGroup.values());
        VerticalLayout createProductForm = new VerticalLayout(productNameField, productGroupField, createSaveButton);
        createProductForm.setSpacing(true);
        createProductForm.setPadding(true);

        VerticalLayout deleteProductForm = new VerticalLayout(productIdToDeleteField, deleteButton);
        deleteProductForm.setSpacing(true);
        deleteProductForm.setPadding(true);

        HorizontalLayout formsLayout = new HorizontalLayout(createProductForm, deleteProductForm);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<ProductResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<ProductResponse> products = response.getBody();
        grid.setItems(products);
    }

    private void createProduct() {
        String productName = productNameField.getValue();
        ProductGroup productGroup = productGroupField.getValue();

        CreateProductRequest request = new CreateProductRequest(productName, productGroup);
        restTemplate.postForObject(BASE_URL, request, ProductResponse.class);
        loadData();
    }

    private void deleteProduct() {
        Long productId = Long.parseLong(productIdToDeleteField.getValue());
        restTemplate.delete(BASE_URL + "/" + productId);
        loadData();
    }
}
