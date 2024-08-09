package com.food.manager.backend.repository;

import com.food.manager.backend.entity.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g JOIN g.shoppingListItems i WHERE i.itemId = :itemId")
    Group findByItem(@Param("itemId") Long itemId);

    @Query("SELECT g FROM Group g JOIN FETCH g.users WHERE g.groupId = :groupId")
    Group findGroupWithUsers(@Param("groupId") Long groupId);
}
