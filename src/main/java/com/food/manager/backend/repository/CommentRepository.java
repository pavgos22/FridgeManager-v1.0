package com.food.manager.backend.repository;

import com.food.manager.backend.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
