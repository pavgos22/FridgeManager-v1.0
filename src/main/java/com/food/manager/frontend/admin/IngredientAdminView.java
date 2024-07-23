package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.IngredientResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    public IngredientAdminView() {
        setupGrid();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("ingredientId", "quantityType", "quantity", "required", "ignoreGroup", "productId", "recipeId");
        add(grid);
    }

    private void loadData() {
        ResponseEntity<List<IngredientResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<IngredientResponse> ingredients = response.getBody();
        grid.setItems(ingredients);
    }
}
