package com.food.manager.backend.service;

import com.food.manager.backend.config.WeatherService;
import com.food.manager.backend.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.backend.dto.response.RecipeNutrition;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.entity.Ingredient;
import com.food.manager.backend.entity.Nutrition;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.entity.Recipe;
import com.food.manager.backend.enums.QuantityType;
import com.food.manager.backend.enums.Weather;
import com.food.manager.backend.exception.DuplicateIngredientException;
import com.food.manager.backend.exception.IngredientsNotFoundException;
import com.food.manager.backend.exception.RecipeNotFoundException;
import com.food.manager.backend.mapper.RecipeMapper;
import com.food.manager.backend.repository.IngredientRepository;
import com.food.manager.backend.repository.RecipeRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RecipeMapper recipeMapper;

    public RecipeResponse getRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            return recipeMapper.toRecipeResponse(recipeOptional.get());
        } else {
            throw new RecipeNotFoundException(id);
        }
    }

    public List<RecipeResponse> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipeMapper.mapToRecipeResponseList(recipes);
    }

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        Set<Long> ingredientIdSet = new HashSet<>(createRecipeRequest.ingredientIds());
        if (ingredientIdSet.size() != createRecipeRequest.ingredientIds().size()) {
            throw new DuplicateIngredientException();
        }

        Recipe recipe = new Recipe(createRecipeRequest.recipeName(), createRecipeRequest.description(), createRecipeRequest.recipeType(), createRecipeRequest.weather(), createRecipeRequest.recipeURL());

        recipeRepository.save(recipe);

        List<Ingredient> ingredients = ingredientRepository.findAllById(createRecipeRequest.ingredientIds());
        if (ingredients.size() != createRecipeRequest.ingredientIds().size()) {
            throw new IngredientsNotFoundException();
        }

        Set<Ingredient> uniqueIngredients = new HashSet<>(ingredients);
        recipe.setIngredients(uniqueIngredients);

        for (Ingredient ingredient : uniqueIngredients) {
            ingredient.setRecipe(recipe);
            ingredientRepository.save(ingredient);
        }

        recipeRepository.save(recipe);

        return recipeMapper.toRecipeResponse(recipe);
    }


    public Weather fetchWeatherFromApi() {
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Warsaw?unitGroup=metric&key=" + weatherService.getWeatherKey() + "&contentType=json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONObject currentConditions = jsonResponse.getJSONObject("currentConditions");

        double temperature = currentConditions.getDouble("temp");

        if (currentConditions.getDouble("snow") > 0)
            return Weather.SNOWY;
         else if (currentConditions.getDouble("precip") > 0)
            return Weather.RAINY;
         else if (temperature >= 30)
            return Weather.HOT;
         else if (temperature >= 16)
            return Weather.WARM;
        else if (temperature > 0)
            return Weather.COLD;
        else
            return Weather.FREEZING;
    }

    public List<RecipeResponse> getRecipesForCurrentWeather() {
        Weather currentWeather = fetchWeatherFromApi();
        List<Recipe> recipes = recipeRepository.findAll();

        List<Recipe> filteredRecipes = recipes.stream()
                .filter(recipe -> recipe.getWeather() == currentWeather)
                .collect(Collectors.toList());

        return recipeMapper.mapToRecipeResponseList(filteredRecipes);
    }

    public RecipeNutrition calcNutrition(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        int totalCalories = 0;
        float totalProtein = 0;
        float totalFat = 0;
        float totalCarbohydrate = 0;

        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getQuantityType() != QuantityType.GRAM) {
                throw new IllegalArgumentException("Ingredient quantity must be in grams to calculate nutrition.");
            }

            Product product = ingredient.getProduct();
            if (product != null && product.getNutrition() != null) {
                Nutrition nutrition = product.getNutrition();

                float multiplier = ingredient.getQuantity() / 100.0f;

                totalCalories += (int) (nutrition.getCalories() * multiplier);
                totalProtein += nutrition.getProtein() * multiplier;
                totalFat += nutrition.getFat() * multiplier;
                totalCarbohydrate += nutrition.getCarbohydrate() * multiplier;
            }
        }
        return new RecipeNutrition(totalCalories, totalProtein, totalFat, totalCarbohydrate);
    }



    public void deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else {
            throw new RecipeNotFoundException(id);
        }
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }

}
