package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.ProductResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    public ProductAdminView() {
        setupGrid();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("productId", "productName", "nutrition", "recipeIds");
        add(grid);
    }

    private void loadData() {
        ResponseEntity<List<ProductResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<ProductResponse> products = response.getBody();
        grid.setItems(products);
    }
}
