package com.food.manager.backend.repository;

import com.food.manager.backend.entity.Group;
import com.food.manager.backend.entity.Product;
import com.food.manager.backend.entity.ShoppingListItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    Optional<ShoppingListItem> findByProductAndGroup(Product product, Group group);
    List<ShoppingListItem> findByGroupId(Long groupId);
}
