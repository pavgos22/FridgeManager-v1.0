package com.food.manager.frontend.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("admin")
public class AdminView extends VerticalLayout {

    public AdminView() {
        add(new RouterLink("Users", UserAdminView.class));
        add(new RouterLink("Groups", GroupAdminView.class));
//        add(new RouterLink("Fridges", FridgeAdminView.class));
//        add(new RouterLink("Products", ProductAdminView.class));
//        add(new RouterLink("Recipes", RecipeAdminView.class));
//        add(new RouterLink("Shopping List Items", ShoppingListItemAdminView.class));
    }
}
