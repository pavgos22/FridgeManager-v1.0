package com.food.manager.frontend.admin.window;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Map;

public class CommentsWindow extends Dialog {
    public CommentsWindow(Map<String, String> comments) {
        Grid<Map.Entry<String, String>> commentsGrid = new Grid<>();
        commentsGrid.setItems(comments.entrySet());
        commentsGrid.addColumn(Map.Entry::getKey).setHeader("Author");
        commentsGrid.addColumn(Map.Entry::getValue).setHeader("Comment");

        Button closeButton = new Button("Close", e -> close());

        VerticalLayout layout = new VerticalLayout(commentsGrid, closeButton);
        add(layout);
        setWidth("600px");
        setHeight("400px");
        open();
    }
}
