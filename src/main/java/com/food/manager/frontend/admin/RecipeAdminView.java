package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.dto.response.RecipeNutrition;
import com.food.manager.frontend.admin.window.RecipeListWindow;
import com.food.manager.frontend.admin.window.RecipeNutritionWindow;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

    private final TextField recipeIdField = new TextField("Recipe ID");
    private final Button getNutritionButton = new Button("Get Nutrition");
    private final Button getWeatherRecipesButton = new Button("Get Recipes for Current Weather");

    public RecipeAdminView() {
        setupGrid();
        setupForm();
        loadData();
    }

    private void setupGrid() {
        grid.setColumns("recipeId", "recipeName", "description", "recipeType", "weather", "recipeUrl");
        add(grid);
    }

    private void setupForm() {
        getNutritionButton.addClickListener(e -> getRecipeNutrition());
        getWeatherRecipesButton.addClickListener(e -> getRecipesForCurrentWeather());

        VerticalLayout nutritionForm = new VerticalLayout(recipeIdField, getNutritionButton, getWeatherRecipesButton);
        nutritionForm.setSpacing(true);
        nutritionForm.setPadding(true);

        HorizontalLayout formsLayout = new HorizontalLayout(nutritionForm);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<RecipeResponse>> response = restTemplate.exchange(BASE_URL, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<RecipeResponse> recipes = response.getBody();
        grid.setItems(recipes);
    }

    private void getRecipeNutrition() {
        long recipeId = Long.parseLong(recipeIdField.getValue());
        ResponseEntity<RecipeNutrition> response = restTemplate.getForEntity(BASE_URL + "/" + recipeId + "/nutrition", RecipeNutrition.class);
        RecipeNutrition nutrition = response.getBody();
        new RecipeNutritionWindow(nutrition);
    }

    private void getRecipesForCurrentWeather() {
        ResponseEntity<List<RecipeResponse>> response = restTemplate.exchange(BASE_URL + "/weather", org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<List<RecipeResponse>>() {});
        List<RecipeResponse> recipes = response.getBody();
        new RecipeListWindow(recipes).open();
    }
}

