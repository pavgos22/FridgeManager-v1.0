package com.food.manager.repository;

import com.food.manager.entity.Group;
import com.food.manager.entity.Product;
import com.food.manager.entity.ShoppingListItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    Optional<ShoppingListItem> findByProductAndGroup(Product product, Group group);
}
