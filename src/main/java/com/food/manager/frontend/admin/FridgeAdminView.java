package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.FridgeResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    public FridgeAdminView() {
        setupGrid();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("fridgeId", "group", "fridgeProducts");
        add(grid);
    }

    private void loadData() {
        ResponseEntity<List<FridgeResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<FridgeResponse> fridges = response.getBody();
        grid.setItems(fridges);
    }
}
