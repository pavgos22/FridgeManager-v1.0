package com.food.manager.backend.service;

import com.food.manager.backend.config.OAuthService;
import com.food.manager.backend.dto.request.product.CreateProductRequest;
import com.food.manager.backend.dto.request.product.UpdateProductRequest;
import com.food.manager.backend.dto.response.NutritionResponse;
import com.food.manager.backend.dto.response.ProductResponse;
import com.food.manager.backend.entity.Nutrition;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.exception.NutritionIsNullException;
import com.food.manager.backend.exception.ProductNotFoundInProductsException;
import com.food.manager.backend.mapper.NutritionMapper;
import com.food.manager.backend.mapper.ProductMapper;
import com.food.manager.backend.repository.NutritionRepository;
import com.food.manager.backend.repository.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private NutritionRepository nutritionRepository;
    @Autowired
    private NutritionMapper nutritionMapper;

    public ProductResponse getProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productMapper.toProductResponse(productOptional.get());
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public NutritionResponse getProductNutrition(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product with id " + productId + " not found"));
        Nutrition nutrition = nutritionRepository.findById(product.getNutrition().getNutritionId()).orElseThrow(() -> new NutritionIsNullException("Nutrition for product with id " + productId + " is Null"));
        return nutritionMapper.toNutritionResponse(nutrition);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.mapToProductList(products);
    }

    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Product product = fetchProductFromAPI(createProductRequest.productName());
        product.setProductGroup(createProductRequest.productGroup());
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(UpdateProductRequest updateProductRequest) {
        Optional<Product> productOptional = productRepository.findById(updateProductRequest.productId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setProductName(updateProductRequest.productName());
            product = productRepository.save(product);
            return productMapper.toProductResponse(product);
        } else {
            throw new RuntimeException("Product not found with id: " + updateProductRequest.productId());
        }
    }

    public Product fetchProductFromAPI(String productName) {
        String token = oAuthService.getOAuthToken();

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://platform.fatsecret.com/rest/server.api?method=foods.search.v3&search_expression=" + productName + "&format=json&include_sub_categories=true&flag_default_serving=true&max_results=10&language=en&region=US";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray foods = jsonResponse.getJSONObject("foods_search").getJSONObject("results").getJSONArray("food");

        for (int i = 0; i < foods.length(); i++) {
            JSONObject food = foods.getJSONObject(i);
            if (food.getString("food_type").equals("Generic") || food.getString("food_type").equals("Brand")) {
                Product product = new Product(food.getString("food_name"));

                Nutrition nutrition = fetchNutritionFromAPI(productName);
                if (nutrition != null)
                    product.setNutrition(nutrition);
                return product;
            }
        }
        return null;
    }

    public Nutrition fetchNutritionFromAPI(String productName) {
        String token = oAuthService.getOAuthToken();

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://platform.fatsecret.com/rest/server.api?method=foods.search.v3&search_expression=" + productName + "&format=json&include_sub_categories=true&flag_default_serving=true&max_results=10&language=en&region=US";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray foods = jsonResponse.getJSONObject("foods_search").getJSONObject("results").getJSONArray("food");

        for (int i = 0; i < foods.length(); i++) {
            JSONObject food = foods.getJSONObject(i);
            if (food.getString("food_type").equals("Generic") || food.getString("food_type").equals("Brand")) {
                JSONArray servings = food.getJSONObject("servings").getJSONArray("serving");
                for (int j = 0; j < servings.length(); j++) {
                    JSONObject serving = servings.getJSONObject(j);
                    if (serving.getString("metric_serving_unit").equals("g") && serving.getDouble("metric_serving_amount") == 100.0) {
                        int calories = serving.getInt("calories");
                        float protein = (float) serving.getDouble("protein");
                        float fat = (float) serving.getDouble("fat");
                        float carbohydrate = (float) serving.getDouble("carbohydrate");
                        return new Nutrition(calories, protein, fat, carbohydrate);
                    }
                }
            }
        }
        return null;
    }

    public void createProductFromWishlist(String productName) {
        Product product = fetchProductFromAPI(productName);
        if (product == null) {
            throw new ProductNotFoundInProductsException("Product not found in API: " + productName);
        }
        productMapper.toProductResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}