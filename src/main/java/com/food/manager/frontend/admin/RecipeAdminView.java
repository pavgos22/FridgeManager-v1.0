package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.backend.dto.response.RecipeNutrition;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.enums.RecipeType;
import com.food.manager.backend.enums.Weather;
import com.food.manager.frontend.admin.window.RecipeListWindow;
import com.food.manager.frontend.admin.window.RecipeNutritionWindow;
import com.vaadin.flow.component.button.Button;
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
import java.util.Set;
import java.util.stream.Collectors;

@Route("admin/recipes")
public class RecipeAdminView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/recipes";
    private final Grid<RecipeResponse> grid = new Grid<>(RecipeResponse.class);

    private final TextField recipeNameField = new TextField("Recipe Name");
    private final TextField descriptionField = new TextField("Description");
    private final ComboBox<RecipeType> recipeTypeField = new ComboBox<>("Recipe Type");
    private final ComboBox<Weather> weatherField = new ComboBox<>("Weather");
    private final TextField ingredientIdsField = new TextField("Ingredient IDs (comma separated)");
    private final TextField recipeUrlField = new TextField("Recipe URL");
    private final Button createRecipeButton = new Button("Create Recipe");

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
        recipeTypeField.setItems(RecipeType.values());
        weatherField.setItems(Weather.values());

        createRecipeButton.addClickListener(e -> createRecipe());
        getNutritionButton.addClickListener(e -> getRecipeNutrition());
        getWeatherRecipesButton.addClickListener(e -> getRecipesForCurrentWeather());

        VerticalLayout createRecipeForm = new VerticalLayout(
                recipeNameField,
                descriptionField,
                recipeTypeField,
                weatherField,
                ingredientIdsField,
                recipeUrlField,
                createRecipeButton
        );
        createRecipeForm.setSpacing(true);
        createRecipeForm.setPadding(true);

        VerticalLayout nutritionForm = new VerticalLayout(recipeIdField, getNutritionButton, getWeatherRecipesButton);
        nutritionForm.setSpacing(true);
        nutritionForm.setPadding(true);

        HorizontalLayout formsLayout = new HorizontalLayout(createRecipeForm, nutritionForm);
        add(formsLayout);
    }

    private void loadData() {
        ResponseEntity<List<RecipeResponse>> response = restTemplate.exchange(
                BASE_URL,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RecipeResponse>>() {}
        );
        List<RecipeResponse> recipes = response.getBody();
        grid.setItems(recipes);
    }

    private void createRecipe() {
        String recipeName = recipeNameField.getValue();
        String description = descriptionField.getValue();
        RecipeType recipeType = recipeTypeField.getValue();
        Weather weather = weatherField.getValue();
        Set<Long> ingredientIds = Set.of(ingredientIdsField.getValue().split(","))
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        String recipeUrl = recipeUrlField.getValue();

        CreateRecipeRequest request = new CreateRecipeRequest(
                recipeName,
                description,
                recipeType,
                weather,
                ingredientIds,
                recipeUrl
        );

        restTemplate.postForObject(BASE_URL, request, RecipeResponse.class);
        loadData();
    }

    private void getRecipeNutrition() {
        long recipeId = Long.parseLong(recipeIdField.getValue());
        ResponseEntity<RecipeNutrition> response = restTemplate.getForEntity(
                BASE_URL + "/" + recipeId + "/nutrition",
                RecipeNutrition.class
        );
        RecipeNutrition nutrition = response.getBody();
        new RecipeNutritionWindow(nutrition).open();
    }

    private void getRecipesForCurrentWeather() {
        ResponseEntity<List<RecipeResponse>> response = restTemplate.exchange(
                BASE_URL + "/weather",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RecipeResponse>>() {}
        );
        List<RecipeResponse> recipes = response.getBody();
        new RecipeListWindow(recipes).open();
    }
}
