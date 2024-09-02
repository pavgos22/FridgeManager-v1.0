# Fridge Manager Application API v1

## Overview

The Food Manager application is a comprehensive `REST API` developed in `Java Spring Boot`. It offers a wide range of functionalities centered around managing food products, recipes, and fridges within user-defined groups. The system supports interactions with external APIs, such as `FatSecretAPI` for nutritional information and `Visual Crossing API` for weather-based recipe recommendations.

## Features

### 1. Group and Fridge Management
- **Group Creation**: Users can create and manage groups.
- **Fridge Assignment**: Each group is assigned its own fridge to store products.
- **Product Management**: Products can be added to the fridge from a pre-populated Products table.

### 2. Wishlist and Product Integration
- **Product Wishlist**: Users can add products to a wishlist if they are not available in the Products table.
- **Automatic Product Addition**: A scheduler processes the wishlist daily at midnight, fetching products from the `FatSecretAPI` and adding them to the Products table.

### 3. Shopping List Management
- **Shopping List**: Each group has a shopping list where members can add or remove products.
- **Commenting**: Members can comment on items in the shopping list.

### 4. Quantity Management
- **QuantityType Handling**: Products have specific QuantityTypes such as `liters`, `milliliters`, `grams`, or `pieces`. Adding or removing product quantities requires matching the QuantityType. Conversion of quantity types will be added in the future.

### 5. Recipe Management
- **Recipe Database**: A collection of recipes is available in the database (for now it's mock data). Admin can add recipes using ingredients that are made with products with specific quantities.
- **Recipe and Fridge Integration**: The application checks which recipes can be made with the available products in the fridge. After a recipe is executed, corresponding amounts of products are removed from the fridge.
- **Weather-Based Recommendations**: Users can view recipes suitable for the current weather, leveraging data from the Visual Crossing API.

### 6. Nutritional Information
- **Nutrition Calculation**: Products have associated nutrition information per 100g. The application can calculate the total calories and macronutrients for each recipe based on the ingredients used.

## Views

### Admin Panel
- The current version includes an admin panel where administrators can manually manage data within the database. This includes adding or removing products, recipes, and managing other resources. The frontend is made with Vaadin which is an open-source Java platform for building web applications with intuitive UI components and just a simple way to display data.

## Future Enhancements

- **User Authentication**: Plan to add user authentication and make a full working app.
- **Expanded Client Frontend**: The frontend will be expanded to include a client panel where users can interact with their data more freely.
- **Personal Fridges**: Users will be allowed to have individual fridges if they are not part of a group.
- **Enhanced Shopping List Management**: Planned features include the ability to remove items from the shopping list using a checkbox and relaxing the restriction on matching quantity types.
- **Autocomplete Search**: The search functionality for products will be enhanced with autocomplete features.
- **User-Generated Recipes**: Users will be able to add their own recipes, subject to verification.
- **Advanced Recipe Features**: The application will start using the `IgnoreGroup` and `Required` fields in recipes for more complex recipe management.
    - **IgnoreGroup**: Products can be assigned to productGroups e.g. Canola Oil, Avocado Oil and Peanut Oil are assigned to `ProductGroup.OIL`, because usually in a recipe these ingredients are replaceable, so if you want to specify that in your recipe a specific type of oil must be used, you can check the ignoreGroup field.
    - **Required**: An ingredient like parsley to garnish can be optional, so then the `required` should be unchecked.
<<<<<<< HEAD
=======
- **API Documentation**: A full documentation for all the endpoints in API.
>>>>>>> 1552743e391f0113efe5cb452cdec38fe5cd811a

## Testing

Unit tests are implemented for the backend, covering entities, controllers, and services.

## Technology Stack

- **Backend**: `Java 21`, `Spring Boot 3.3.2`, `Hibernate`
- **Database**: `MySQL`
- **Frontend**: `Vaadin` (Admin Panel)
- **External APIs**: `FatSecretAPI`, `Visual Crossing API`
<<<<<<< HEAD
- **Dependency Management**: `Gradle 8.8`
=======
- **Dependency Management**: `Gradle 8.8`

## How to run
1. Clone the repository or download ZIP
2. Run `FridgeManagerApplication.java`
3. To run the API type `localhost:8080/v1/...` in Postman or other API testing platform (API documentation coming soon).
4. To run AdminPanel simply type `http://localhost:8080/admin` in browser, the `Vaadin` framework is already installed in the repository.
>>>>>>> 1552743e391f0113efe5cb452cdec38fe5cd811a
