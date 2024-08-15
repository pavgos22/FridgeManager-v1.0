Application Functionality

Application is a fully functional Rest API made with java.
Users can create groups, and each group has its own fridge, to which products can be added from the Products table. The admin manually populates the Products table by pulling items from an external API, FatSecretAPI. If a user wants to add a product that is not in the Products table, they can add it to the wishlist in the Products section. The program, using a scheduler, reads the wishlist daily at midnight, fetches the products from the API, and adds them to the Products table.

Each group has its own shopping list, where products can be added and removed. Each item on the list can be commented on by group members.

Every product has a specified QuantityType — liters, milliliters, grams, or pieces. To add or remove a product quantity, it must match the same QuantityType.

There is a recipe database. Each group can display which recipes can be made from the products available in the fridge. Once a recipe is made, the corresponding amounts of products are removed from the fridge. Users can also display all recipes suitable for the current weather, which is retrieved using the Visual Crossing API.

Products in the database have calories and macronutrients per 100g. The calories and macronutrients for each recipe can be calculated using the calcNutrition method.

Views

In this version of the application, only the admin panel is implemented, where data can be manually added and removed from the database. Future plans include adding user authentication and a functional client panel.

Design Patterns

The project uses a demonstrative database connection via the Singleton design pattern in the SchedulerService class. In the ProductService, the fetchProductFromAPI() method uses the Builder design pattern.

Testing

Unit tests are only implemented for the backend, covering entities, controllers, and services.

Plans for Application Expansion

A single fridge for users without a group
User authentication
Expanded client frontend
Removing items from the list using a checkbox
Quantity type does not need to match
Refactoring of endpoints
Code refactoring
Autocomplete search for products in the Products table
Allowing users to add their own recipes and verifying them
Using IgnoreGroup and Required fields in recipes
