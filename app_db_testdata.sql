-- MySQL dump 10.13  Distrib 8.4.0, for Win64 (x86_64)
--
-- Host: localhost    Database: app_db
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `CREATED_AT` timestamp NOT NULL,
  `UPDATED_AT` timestamp NULL DEFAULT NULL,
  `ITEM_ID` bigint DEFAULT NULL,
  `USER_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `ITEM_ID` (`ITEM_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`ITEM_ID`) REFERENCES `shopping_list_items` (`ITEM_ID`),
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (54,'asdf','2024-08-14 00:33:58','2024-08-14 00:33:58',201,52),(55,'bonn','2024-08-14 00:34:02','2024-08-14 00:34:02',201,52);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fridge_products`
--

DROP TABLE IF EXISTS `fridge_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fridge_products` (
  `FRIDGE_PRODUCT_ID` bigint NOT NULL AUTO_INCREMENT,
  `quantity_type` tinyint NOT NULL,
  `QUANTITY` int DEFAULT NULL,
  `FRIDGE_ID` bigint DEFAULT NULL,
  `PRODUCT_ID` bigint NOT NULL,
  PRIMARY KEY (`FRIDGE_PRODUCT_ID`),
  KEY `FRIDGE_ID` (`FRIDGE_ID`),
  KEY `FKfmluil80f0gd2sn1w6ckgp9oi` (`PRODUCT_ID`),
  CONSTRAINT `FKfmluil80f0gd2sn1w6ckgp9oi` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `products` (`PRODUCT_ID`),
  CONSTRAINT `fridge_products_ibfk_1` FOREIGN KEY (`FRIDGE_ID`) REFERENCES `fridges` (`FRIDGE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fridge_products`
--

LOCK TABLES `fridge_products` WRITE;
/*!40000 ALTER TABLE `fridge_products` DISABLE KEYS */;
INSERT INTO `fridge_products` VALUES (73,0,575,10,72),(76,0,30,9,103),(77,3,4,9,100),(78,0,500,9,80),(80,0,40,11,104),(81,0,40,11,105),(82,0,5,10,105),(83,0,15,10,109),(85,1,500,12,60);
/*!40000 ALTER TABLE `fridge_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fridges`
--

DROP TABLE IF EXISTS `fridges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fridges` (
  `FRIDGE_ID` bigint NOT NULL AUTO_INCREMENT,
  `GROUP_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`FRIDGE_ID`),
  KEY `GROUP_ID` (`GROUP_ID`),
  CONSTRAINT `fridges_ibfk_1` FOREIGN KEY (`GROUP_ID`) REFERENCES `groups` (`GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fridges`
--

LOCK TABLES `fridges` WRITE;
/*!40000 ALTER TABLE `fridges` DISABLE KEYS */;
INSERT INTO `fridges` VALUES (8,27),(9,28),(10,29),(11,30),(12,31),(15,269),(16,270);
/*!40000 ALTER TABLE `fridges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups` (
  `GROUP_ID` bigint NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` varchar(255) NOT NULL,
  `CREATED_AT` timestamp NOT NULL,
  `UPDATED_AT` timestamp NOT NULL,
  PRIMARY KEY (`GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (27,'super group','2024-07-13 23:13:36','2024-07-13 23:13:36'),(28,'super group','2024-07-13 23:16:24','2024-07-13 23:16:24'),(29,'great group','2024-07-13 23:19:58','2024-07-13 23:19:58'),(30,'xyz','2024-07-22 22:00:53','2024-07-22 22:00:53'),(31,'xyz','2024-07-22 22:05:45','2024-07-22 22:05:45'),(269,'testgroup1','2024-08-14 14:19:28','2024-08-14 14:19:28'),(270,'testgroup2','2024-08-14 14:20:00','2024-08-14 14:20:00');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredients` (
  `INGREDIENT_ID` bigint NOT NULL AUTO_INCREMENT,
  `quantity_type` tinyint DEFAULT NULL,
  `QUANTITY` int NOT NULL,
  `REQUIRED` tinyint(1) NOT NULL,
  `IGNORE_GROUP` tinyint(1) NOT NULL,
  `PRODUCT_ID` bigint DEFAULT NULL,
  `RECIPE_ID` bigint DEFAULT NULL,
  PRIMARY KEY (`INGREDIENT_ID`),
  KEY `FK_PRODUCT` (`PRODUCT_ID`),
  KEY `FK_RECIPE` (`RECIPE_ID`),
  CONSTRAINT `FK_PRODUCT` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `products` (`PRODUCT_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_RECIPE` FOREIGN KEY (`RECIPE_ID`) REFERENCES `recipes` (`RECIPE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredients`
--

LOCK TABLES `ingredients` WRITE;
/*!40000 ALTER TABLE `ingredients` DISABLE KEYS */;
INSERT INTO `ingredients` VALUES (13,0,10,1,0,101,13),(14,0,10,1,0,102,13),(15,0,10,1,0,103,13),(16,0,10,1,0,104,16),(17,0,10,1,0,105,16),(18,0,100,1,1,106,105),(19,0,100,1,1,107,105),(20,0,100,1,1,106,105),(142,0,200,1,0,559,106),(143,0,200,1,0,57,107),(144,0,250,1,0,62,107),(145,0,80,1,0,66,107);
/*!40000 ALTER TABLE `ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nutrition`
--

DROP TABLE IF EXISTS `nutrition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nutrition` (
  `NUTRITION_ID` bigint NOT NULL AUTO_INCREMENT,
  `CALORIES` int NOT NULL,
  `PROTEIN` float NOT NULL,
  `FAT` float NOT NULL,
  `CARBOHYDRATE` float NOT NULL,
  PRIMARY KEY (`NUTRITION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nutrition`
--

LOCK TABLES `nutrition` WRITE;
/*!40000 ALTER TABLE `nutrition` DISABLE KEYS */;
INSERT INTO `nutrition` VALUES (13,52,0.26,0.17,13.81),(14,86,3.22,1.18,19.02),(15,195,29.55,7.72,0),(16,43,0.46,0,3.55),(17,231,0,0,0),(18,47,0.94,0.12,11.75),(21,69,0.72,0.16,18.1),(22,58,0.38,0.12,15.46),(26,33,0.68,0.3,7.98),(28,313,7.61,8.36,74.97),(29,23,2.86,0.39,3.63),(30,85,0.07,0,2.61),(32,89,1.09,0.33,22.84),(34,32,0.67,0.3,7.68),(35,263,16.88,21.19,0),(36,136,20.54,5.41,0),(38,41,0.93,0.24,9.58),(39,32,1.83,0.19,7.34),(41,80,1.82,0.75,17.77),(42,53,6.28,0.04,7.61),(43,53,6.28,0.04,7.61),(47,0,0,0,0),(48,381,0.26,0.05,91.27),(49,105,6.11,3.81,11.27),(50,105,6.11,3.81,11.27),(51,53,6.28,0.04,7.61),(52,884,0,100,0),(53,884,0,100,0),(54,129,2.66,0.28,27.9),(55,304,0.3,0,82.4),(56,101,5.3,5.59,7.75),(57,314,12.26,16.76,54.66),(58,296,10.4,2.12,68.61),(59,255,10.95,3.26,64.81),(60,567,16.96,48,26.04),(61,567,16.96,48,26.04),(62,289,14.76,12.95,55.74),(63,264,25.53,17.2,0),(64,23,2.13,0.52,3.67),(65,23,2.13,0.52,3.67),(66,149,6.36,0.5,33.06),(67,884,0,100,0),(68,245,24.85,15.36,0),(69,3,0.35,0.08,0.33),(70,5,0.39,0.12,0.62),(71,145,4.58,7.37,15.66),(72,110,2.56,0.89,22.78),(73,329,6.42,0.59,72.03),(74,121,3.54,0.38,25.22),(75,61,1.5,0.3,14.15),(76,306,11,10.25,64.43),(78,34,2.82,0.37,6.64),(79,86,3.22,1.18,19.02),(80,15,0.65,0.11,3.63),(82,18,0.62,0.19,4.12),(83,147,12.58,9.94,0.77),(84,322,15.86,26.54,3.59),(85,52,10.9,0.17,0.73),(86,81,5.42,0.4,14.46),(87,52,1.2,0.65,11.94),(88,63,1.06,0.2,16.01),(89,656,14.32,66.43,12.27),(90,654,15.23,65.21,13.71),(93,458,9,27.74,47.81),(94,578,21.26,50.64,19.74),(95,569,21.35,45.97,27),(96,163,16.6,8.6,3.83),(97,403,24.9,33.14,1.28),(98,24,1.01,0.19,5.7),(99,260,0.54,0.14,66.63),(100,103,12.49,4.51,2.68),(101,201,3.52,10.72,24.4),(193,51,1.35,0.25,10.92),(194,30,3.27,0.73,4.35),(195,30,0.61,0.15,7.55);
/*!40000 ALTER TABLE `nutrition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `PRODUCT_ID` bigint NOT NULL AUTO_INCREMENT,
  `PRODUCT_NAME` varchar(255) NOT NULL,
  `NUTRITION_ID` bigint DEFAULT NULL,
  `product_group` enum('OIL','POULTRY','TEST') DEFAULT NULL,
  PRIMARY KEY (`PRODUCT_ID`),
  UNIQUE KEY `unique_product_name` (`PRODUCT_NAME`),
  KEY `NUTRITION_ID` (`NUTRITION_ID`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`NUTRITION_ID`) REFERENCES `nutrition` (`NUTRITION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=617 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (57,'Apples',13,NULL),(58,'Corn',14,NULL),(59,'Chicken Breast',15,NULL),(60,'Beer',16,NULL),(61,'Vodka',17,NULL),(62,'Oranges',18,NULL),(65,'Grapes (Red or Green, European Type Varieties Such As Thompson Seedless)',21,NULL),(66,'Pears',22,NULL),(70,'Berries',26,NULL),(72,'Bay Leaf',28,NULL),(73,'Spinach',29,NULL),(74,'Red Table Wine',30,NULL),(76,'Bananas',32,NULL),(78,'Strawberries',34,NULL),(79,'Ground Pork',35,NULL),(80,'Pork Loin (Tenderloin)',36,NULL),(82,'Carrots',38,NULL),(83,'Scallions or Spring Onions',39,NULL),(85,'Ginger',41,NULL),(86,'Soy Sauce',42,NULL),(87,'Soy Sauce (Shoyu)',43,NULL),(91,'Water',47,NULL),(92,'Cornstarch',48,NULL),(93,'Fish in Lemon-Butter Sauce with Starch Item and Vegetable (Frozen Meal)',49,NULL),(94,'Potato Starch',50,NULL),(95,'Premium Dark Soy Sauce',51,NULL),(96,'Canola Vegetable Oil',52,NULL),(97,'Vegetable Oil',53,NULL),(98,'Rice Vinegar',54,NULL),(99,'Honey',55,NULL),(100,'Chili',56,NULL),(101,'Chili Powder',57,NULL),(102,'White Pepper',58,NULL),(103,'Black Pepper',59,NULL),(104,'Black Sesame Seeds',60,NULL),(105,'Toasted Sesame Seeds',61,NULL),(106,'Paprika',62,NULL),(107,'Ground Meat',63,NULL),(108,'Cilantro (Coriander)',64,NULL),(109,'Cilantro',65,NULL),(110,'Garlic',66,NULL),(111,'Rapeseed Oil',67,NULL),(112,'Chicken Thigh',68,NULL),(113,'Beef Stock',69,NULL),(114,'Chicken Stock',70,NULL),(115,'Vegetable Frittata',71,NULL),(116,'Brown Rice',72,NULL),(117,'Jasmine Rice',73,NULL),(118,'Basmati Rice',74,NULL),(119,'Leeks',75,NULL),(120,'Oregano',76,NULL),(122,'Broccoli',78,NULL),(123,'Yellow Sweet Corn',79,NULL),(124,'Cucumber (with Peel)',80,NULL),(126,'Pickles',82,NULL),(127,'Egg',83,NULL),(128,'Egg Yolk',84,NULL),(129,'Egg White',85,NULL),(130,'Green Peas',86,NULL),(131,'Raspberries',87,NULL),(132,'Sweet Cherries',88,NULL),(133,'Brazil Nuts',89,NULL),(134,'Walnuts',90,NULL),(137,'Vanilla Paste',93,NULL),(138,'Almonds',94,NULL),(139,'Pistachio',95,NULL),(140,'Sliced Ham  (Regular, Approx. 11% Fat)',96,NULL),(141,'Cheddar Cheese',97,NULL),(142,'Eggplant',98,NULL),(143,'Jam Preserves',99,NULL),(144,'4% Small Curd Cottage Cheese',100,NULL),(145,'Ice Cream',101,NULL),(557,'Oyster Sauce',193,NULL),(558,'Chives',194,NULL),(559,'Watermelon',195,NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipes`
--

DROP TABLE IF EXISTS `recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipes` (
  `RECIPE_ID` bigint NOT NULL AUTO_INCREMENT,
  `recipe_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `recipe_type` enum('AMERICAN','ITALIAN','TEST') NOT NULL,
  `weather` enum('COLD','FREEZING','HOT','RAINY','SNOWY','WARM') DEFAULT NULL,
  `RECIPE_URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`RECIPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipes`
--

LOCK TABLES `recipes` WRITE;
/*!40000 ALTER TABLE `recipes` DISABLE KEYS */;
INSERT INTO `recipes` VALUES (12,'pepper recipe','easy recipe for people who love pepper!','TEST',NULL,NULL),(13,'pepper recipeeee','easy recipe for people who love pepper!','TEST',NULL,NULL),(14,'sesame','easy recipe for people who love sesame seeds!','TEST',NULL,NULL),(15,'sesameee','easy recipe for people who love sesame seeds!','TEST',NULL,NULL),(16,'sesameee','easy recipe for people who love sesame seeds!','TEST',NULL,NULL),(17,'sesameee','easy recipe for people who love sesame seeds!','TEST',NULL,NULL),(18,'Test Recipe','Delicious test recipe','TEST','FREEZING','http://test-recipe.url'),(35,'New Test Recipe','New delicious test recipe','TEST','WARM','http://new-test-recipe.url'),(105,'testrecipe1','new recipe','TEST','WARM','testurl'),(106,'watermelon recipe','200 grams of watermelon','TEST','HOT','testurl'),(107,'Fruit Salad','a simple fruit salad','TEST','WARM','');
/*!40000 ALTER TABLE `recipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_list_items`
--

DROP TABLE IF EXISTS `shopping_list_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shopping_list_items` (
  `ITEM_ID` bigint NOT NULL AUTO_INCREMENT,
  `QUANTITY` int DEFAULT NULL,
  `CHECKED` tinyint(1) NOT NULL,
  `PRODUCT_ID` bigint DEFAULT NULL,
  `quantity_type` enum('GRAM','LITER','MILLILITER','PIECE') NOT NULL,
  `GROUP_ID` bigint NOT NULL,
  PRIMARY KEY (`ITEM_ID`),
  KEY `FKl2chnyyp1gbvqmd1nliwdquf1` (`PRODUCT_ID`),
  KEY `FKenox1bq312svl10s7hsqkslym` (`GROUP_ID`),
  CONSTRAINT `FKenox1bq312svl10s7hsqkslym` FOREIGN KEY (`GROUP_ID`) REFERENCES `groups` (`GROUP_ID`),
  CONSTRAINT `FKl2chnyyp1gbvqmd1nliwdquf1` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `products` (`PRODUCT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_list_items`
--

LOCK TABLES `shopping_list_items` WRITE;
/*!40000 ALTER TABLE `shopping_list_items` DISABLE KEYS */;
INSERT INTO `shopping_list_items` VALUES (9,3,0,58,'MILLILITER',28),(10,20,0,60,'MILLILITER',28),(12,5,0,70,'MILLILITER',28),(15,5,0,74,'MILLILITER',28),(16,1,0,66,'MILLILITER',29),(17,1,0,66,'MILLILITER',29),(18,1,0,66,'MILLILITER',29),(19,1,0,66,'MILLILITER',29),(20,1,0,66,'MILLILITER',29),(21,6,0,66,'MILLILITER',28),(24,5,0,57,'MILLILITER',29),(26,100,0,60,'PIECE',30),(27,140,0,62,'PIECE',30),(201,20,0,101,'PIECE',31);
/*!40000 ALTER TABLE `shopping_list_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_group`
--

DROP TABLE IF EXISTS `user_has_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_has_group` (
  `USER_ID` bigint NOT NULL,
  `GROUP_ID` bigint NOT NULL,
  PRIMARY KEY (`USER_ID`,`GROUP_ID`),
  KEY `GROUP_ID` (`GROUP_ID`),
  CONSTRAINT `user_has_group_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`USER_ID`),
  CONSTRAINT `user_has_group_ibfk_2` FOREIGN KEY (`GROUP_ID`) REFERENCES `groups` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_group`
--

LOCK TABLES `user_has_group` WRITE;
/*!40000 ALTER TABLE `user_has_group` DISABLE KEYS */;
INSERT INTO `user_has_group` VALUES (52,31),(225,269),(226,269),(226,270),(227,270);
/*!40000 ALTER TABLE `user_has_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `USER_ID` bigint NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(255) NOT NULL,
  `FIRST_NAME` varchar(255) NOT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `EMAIL` varchar(255) NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  `CREATED_AT` timestamp NOT NULL,
  `UPDATED_AT` timestamp NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=228 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (52,'username','firstName','lastName','email@test.com','password','2024-08-13 22:36:26','2024-08-13 22:36:26'),(225,'testuser1','test1','test1','test1@test.com','securepassword123','2024-08-14 14:17:56','2024-08-14 14:17:56'),(226,'testuser2','test2','test2','test2@test.com','securepassword321','2024-08-14 14:18:08','2024-08-14 14:18:08'),(227,'testuser3','test3','test3','test3@test.com','securepassword456','2024-08-14 14:18:18','2024-08-14 14:18:18');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
INSERT INTO `wishlist` VALUES (13,'Cornstarch'),(14,'Potato Starch');
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-14 16:43:14
