package com.food.manager.frontend.admin.window;

import com.food.manager.backend.dto.response.ShoppingListItemResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ItemWindow extends Dialog {

    public ItemWindow(Grid<ShoppingListItemResponse> itemsGrid) {
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        VerticalLayout layout = new VerticalLayout();
        layout.add(itemsGrid);
        itemsGrid.setSizeFull();

        Button closeButton = new Button("Close", e -> this.close());
        layout.add(closeButton);
        layout.setSizeFull();

        add(layout);
        open();
    }
}
