package com.food.manager.backend.service;

import com.food.manager.backend.dto.request.recipe.CreateRecipeRequest;
import com.food.manager.backend.dto.response.RecipeResponse;
import com.food.manager.backend.entity.Ingredient;
import com.food.manager.backend.entity.Recipe;
import com.food.manager.backend.enums.Weather;
import com.food.manager.backend.exception.DuplicateIngredientException;
import com.food.manager.backend.exception.IngredientNotFoundException;
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
    private RecipeMapper recipeMapper;

    public RecipeResponse getRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            return recipeMapper.toRecipeResponse(recipeOptional.get());
        } else {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
    }

    public List<RecipeResponse> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipeMapper.mapToRecipeResponseList(recipes);
    }

    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest) {
        Recipe recipe = new Recipe(createRecipeRequest.recipeName(), createRecipeRequest.description(), createRecipeRequest.recipeType(), createRecipeRequest.weather(), createRecipeRequest.recipeURL());

        recipeRepository.save(recipe);

        List<Ingredient> ingredients = ingredientRepository.findAllById(createRecipeRequest.ingredientIds());
        if (ingredients.size() != createRecipeRequest.ingredientIds().size())
            throw new IngredientNotFoundException("One or more ingredients not found");

        Set<Ingredient> uniqueIngredients = new HashSet<>(ingredients);
        if (uniqueIngredients.size() != ingredients.size())
            throw new DuplicateIngredientException("Duplicate ingredients found");

        recipe.setIngredients(uniqueIngredients);

        for (Ingredient ingredient : uniqueIngredients) {
            ingredient.setRecipe(recipe);
            ingredientRepository.save(ingredient);
        }

        recipeRepository.save(recipe);

        return recipeMapper.toRecipeResponse(recipe);
    }

    public Weather fetchWeatherFromApi() {
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Warsaw?include=fcst%2Cobs%2Chistfcst%2Cstats%2Ccurrent&key=XTDHB9DVQHUMW737HZX5YBQYV&options=beta";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONObject currentConditions = jsonResponse.getJSONObject("currentConditions");

        double temperature = currentConditions.getDouble("temp");

        if (currentConditions.getDouble("snow") > 0)
            return Weather.SNOWY;
         else if (currentConditions.getDouble("precip") > 0)
            return Weather.RAINY;
         else if (temperature >= 85)
            return Weather.HOT;
         else if (temperature >= 60)
            return Weather.WARM;
        else if (temperature >= 32)
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

    public void deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }

}
