package com.food.manager.frontend.admin.window;

import com.food.manager.backend.dto.response.RecipeResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RecipeWindow extends Dialog {

    public RecipeWindow(Grid<RecipeResponse> recipeGrid) {
        setWidth("800px");
        setHeight("600px");
        setModal(true);
        setDraggable(true);
        setResizable(true);

        VerticalLayout layout = new VerticalLayout();
        layout.add(recipeGrid);

        Button closeButton = new Button("Close", event -> close());
        layout.add(closeButton);

        add(layout);
        open();
    }
}
