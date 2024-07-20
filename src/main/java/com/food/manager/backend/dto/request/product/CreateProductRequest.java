package com.food.manager.backend.dto.request.product;

import com.food.manager.backend.enums.ProductGroup;

public record CreateProductRequest(String productName, ProductGroup productGroup) {
}
