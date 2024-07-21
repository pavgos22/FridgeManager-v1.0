package com.food.manager.frontend.admin;

import com.food.manager.backend.dto.response.UserResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

@Route("admin/wishlist")
public class WishlistAdminView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/wishlist";
    private final Grid<UserResponse> grid = new Grid<>(UserResponse.class);

    public WishlistAdminView() {

    }
}
