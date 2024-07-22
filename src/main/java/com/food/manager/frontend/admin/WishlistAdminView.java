package com.food.manager.frontend.admin;

import com.food.manager.backend.entity.Wishlist;
import com.food.manager.backend.service.WishlistService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("admin/wishlist")
public class WishlistAdminView extends VerticalLayout {

    private final WishlistService wishlistService;

    private Grid<Wishlist> grid = new Grid<>(Wishlist.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/v1/wishlist";

    public void refresh() {
        grid.setItems(wishlistService.getAllWishlistItems());
    }

    public void refreshC() {
        Wishlist[] wishlistArray = restTemplate.getForObject(BASE_URL, Wishlist[].class);
        List<Wishlist> wishlist = Arrays.asList(wishlistArray);
        grid.setItems(wishlist);
    }

    public WishlistAdminView(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
        grid.setColumns("id", "productName");
        add(grid);
        setSizeFull();
        refreshC();
    }
}
