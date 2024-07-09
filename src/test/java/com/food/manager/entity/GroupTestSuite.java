package com.food.manager.entity;

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
public class GroupTestSuite {

    @Autowired
    private GroupRepository groupRepository;

    private Group group;

    @BeforeEach
    public void setUp() {
        group = new Group();
        group.setGroupName("TestGroup");
        group.setCreatedAt(LocalDateTime.now());
        group.setUpdatedAt(LocalDateTime.now());
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
}
