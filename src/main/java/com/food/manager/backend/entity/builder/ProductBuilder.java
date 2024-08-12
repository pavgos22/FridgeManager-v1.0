package com.food.manager.backend.entity.builder;

import com.food.manager.backend.entity.Nutrition;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.enums.ProductGroup;

public class ProductBuilder {
    private String productName;
    private ProductGroup productGroup;
    private Nutrition nutrition;

    public ProductBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductBuilder withProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
        return this;
    }

    public void withNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public Product build() {
        Product product = new Product(productName, productGroup);
        product.setNutrition(nutrition);
        return product;
    }
}
