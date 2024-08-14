package com.food.manager.frontend.admin.window;

import com.food.manager.backend.dto.response.RecipeResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class RecipeListWindow extends Dialog {

    private final Grid<RecipeResponse> grid;

    public RecipeListWindow(List<RecipeResponse> recipes) {
        this.grid = new Grid<>(RecipeResponse.class);
        setupGrid();
        loadRecipes(recipes);
        setupLayout();
    }

    private void setupGrid() {
        grid.setColumns("recipeId", "recipeName", "description", "recipeType", "weather", "recipeUrl");
        grid.setSizeFull();
    }

    private void loadRecipes(List<RecipeResponse> recipes) {
        grid.setItems(recipes);
    }

    private void setupLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(grid);

        Button closeButton = new Button("Close", event -> close());
        layout.add(closeButton);

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setMargin(true);

        add(layout);
        setModal(true);
        setWidth("600px");
        setHeight("400px");
    }
}
