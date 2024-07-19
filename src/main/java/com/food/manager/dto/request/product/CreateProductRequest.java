package com.food.manager.dto.request.product;

import com.food.manager.enums.ProductGroup;

public record CreateProductRequest(String productName, ProductGroup productGroup) {
}
