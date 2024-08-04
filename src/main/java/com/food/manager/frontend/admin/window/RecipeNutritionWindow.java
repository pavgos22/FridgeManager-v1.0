package com.food.manager.frontend.admin.window;

import com.food.manager.backend.dto.response.RecipeNutrition;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class RecipeNutritionWindow extends Dialog {

    public RecipeNutritionWindow(RecipeNutrition nutrition) {
        setWidth("400px");
        setHeight("300px");
        setModal(true);
        setDraggable(true);
        setResizable(true);

        TextField caloriesField = new TextField("Calories");
        caloriesField.setValue(String.valueOf(nutrition.getCalories()));
        caloriesField.setReadOnly(true);

        TextField proteinField = new TextField("Protein");
        proteinField.setValue(String.valueOf(nutrition.getProtein()));
        proteinField.setReadOnly(true);

        TextField fatField = new TextField("Fat");
        fatField.setValue(String.valueOf(nutrition.getFat()));
        fatField.setReadOnly(true);

        TextField carbohydrateField = new TextField("Carbohydrate");
        carbohydrateField.setValue(String.valueOf(nutrition.getCarbohydrate()));
        carbohydrateField.setReadOnly(true);

        Button closeButton = new Button("Close", event -> close());

        VerticalLayout layout = new VerticalLayout(caloriesField, proteinField, fatField, carbohydrateField, closeButton);
        add(layout);

        open();
    }
}
