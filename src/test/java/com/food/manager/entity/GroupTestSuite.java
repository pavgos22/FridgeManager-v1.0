package com.food.manager.entity;

import com.food.manager.repository.GroupRepository;
import com.food.manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GroupTestSuite {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    private Group group;
    private User user;

    @BeforeEach
    public void setUp() {
        group = new Group();
        group.setGroupName("TestGroup");
        group.setCreatedAt(LocalDateTime.now());
        group.setUpdatedAt(LocalDateTime.now());

        user = new User();
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test.user@example.com");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @Transactional
    public void testCreateGroup() {
        groupRepository.save(group);
        Optional<Group> foundGroup = groupRepository.findById(group.getGroupId());

        assertThat(foundGroup).isPresent();
        assertThat(foundGroup.get().getGroupName()).isEqualTo("TestGroup");
    }

    @Test
    @Transactional
    public void testUpdateGroup() {
        groupRepository.save(group);
        Group savedGroup = groupRepository.findById(group.getGroupId()).orElseThrow();

        savedGroup.setGroupName("UpdatedGroupName");
        savedGroup.setUpdatedAt(LocalDateTime.now());
        groupRepository.save(savedGroup);

        Group updatedGroup = groupRepository.findById(savedGroup.getGroupId()).orElseThrow();
        assertThat(updatedGroup).isNotNull();
        assertThat(updatedGroup.getGroupName()).isEqualTo("UpdatedGroupName");
    }

    @Test
    @Transactional
    public void testDeleteGroup() {
        groupRepository.save(group);
        Group savedGroup = groupRepository.findById(group.getGroupId()).orElseThrow();

        groupRepository.delete(savedGroup);

        Optional<Group> foundGroup = groupRepository.findById(savedGroup.getGroupId());
        assertThat(foundGroup).isNotPresent();
    }

    @Test
    @Transactional
    public void testAddUserToGroup() {
        groupRepository.save(group);
        userRepository.save(user);

        group.getUsers().add(user);
        user.getGroups().add(group);
        groupRepository.save(group);
        userRepository.save(user);

        Group updatedGroup = groupRepository.findById(group.getGroupId()).orElseThrow();
        User updatedUser = userRepository.findById(user.getUserId()).orElseThrow();

        assertThat(updatedGroup.getUsers()).contains(user);
        assertThat(updatedUser.getGroups()).contains(group);
    }

    @Test
    @Transactional
    public void testRemoveUserFromGroup() {
        groupRepository.save(group);
        userRepository.save(user);

        group.getUsers().add(user);
        user.getGroups().add(group);
        groupRepository.save(group);
        userRepository.save(user);

        group.getUsers().remove(user);
        user.getGroups().remove(group);
        groupRepository.save(group);
        userRepository.save(user);

        Group updatedGroup = groupRepository.findById(group.getGroupId()).orElseThrow();
        User updatedUser = userRepository.findById(user.getUserId()).orElseThrow();

        assertThat(updatedGroup.getUsers()).doesNotContain(user);
        assertThat(updatedUser.getGroups()).doesNotContain(group);
    }
}
