package com.food.manager.entity;

import com.food.manager.enums.QuantityType;
import com.food.manager.repository.CommentRepository;
import com.food.manager.repository.ProductRepository;
import com.food.manager.repository.ShoppingListItemRepository;
import com.food.manager.repository.UserRepository;
import com.food.manager.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentTestSuite {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    private Comment comment;
    private ShoppingListItem shoppingListItem;
    private User user;
    private Product product;
    private Group group;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        group = new Group();
        group.setGroupName("TestGroup");
        group.setCreatedAt(LocalDateTime.now());
        group.setUpdatedAt(LocalDateTime.now());
        groupRepository.save(group);

        product = new Product("TestProduct");
        productRepository.save(product);

        shoppingListItem = new ShoppingListItem();
        shoppingListItem.setQuantityType(QuantityType.PIECE);
        shoppingListItem.setQuantity(5);
        shoppingListItem.setChecked(false);
        shoppingListItem.setProduct(product);
        shoppingListItem.setGroup(group);
        shoppingListItemRepository.save(shoppingListItem);

        comment = new Comment();
        comment.setContent("This is a test comment");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setItem(shoppingListItem);
        comment.setAuthor(user);
    }

    @Test
    @Transactional
    public void testCreateComment() {
        commentRepository.save(comment);
        Optional<Comment> foundComment = commentRepository.findById((long) comment.getCommentId());

        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("This is a test comment");
        assertThat(foundComment.get().getItem()).isEqualTo(shoppingListItem);
        assertThat(foundComment.get().getAuthor()).isEqualTo(user);
    }

    @Test
    @Transactional
    public void testUpdateComment() {
        commentRepository.save(comment);
        Comment savedComment = commentRepository.findById((long) comment.getCommentId()).orElseThrow();

        savedComment.setContent("Updated test comment");
        savedComment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(savedComment);

        Comment updatedComment = commentRepository.findById((long) savedComment.getCommentId()).orElseThrow();
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getContent()).isEqualTo("Updated test comment");
    }

    @Test
    @Transactional
    public void testDeleteComment() {
        commentRepository.save(comment);
        Comment savedComment = commentRepository.findById((long) comment.getCommentId()).orElseThrow();

        commentRepository.delete(savedComment);

        Optional<Comment> foundComment = commentRepository.findById((long) savedComment.getCommentId());
        assertThat(foundComment).isNotPresent();
    }
}
