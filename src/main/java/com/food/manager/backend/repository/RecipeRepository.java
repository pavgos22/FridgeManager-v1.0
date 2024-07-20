package com.food.manager.backend.repository;

import com.food.manager.backend.entity.Recipe;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
