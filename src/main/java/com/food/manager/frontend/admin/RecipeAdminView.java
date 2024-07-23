package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.RecipeResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("admin/recipes")
public class RecipeAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/recipes";
    private final Grid<RecipeResponse> grid = new Grid<>(RecipeResponse.class);

    public RecipeAdminView() {
        setupGrid();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("recipeId", "recipeName", "description", "recipeType", "weather", "recipeUrl");
        add(grid);
    }

    private void loadData() {
        ResponseEntity<List<RecipeResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<RecipeResponse> recipes = response.getBody();
        grid.setItems(recipes);
    }
}
