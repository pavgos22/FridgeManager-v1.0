package com.food.manager.backend.repository;

import com.food.manager.backend.entity.FridgeProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Transactional
public interface FridgeProductRepository extends JpaRepository<FridgeProduct, Long> {
    @Query("SELECT fp.product.productName FROM FridgeProduct fp WHERE fp.FridgeProductId = :fridgeProductId")
    Optional<String> findProductNameByFridgeProductId(@Param("fridgeProductId") Long fridgeProductId);
}
