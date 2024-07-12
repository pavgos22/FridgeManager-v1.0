package com.food.manager.service;

import com.food.manager.dto.request.group.AddUserRequest;
import com.food.manager.dto.request.group.CreateGroupRequest;
import com.food.manager.dto.request.group.DeleteGroupRequest;
import com.food.manager.dto.request.group.UpdateGroupRequest;
import com.food.manager.dto.request.user.DeleteUserRequest;
import com.food.manager.dto.response.GroupResponse;
import com.food.manager.entity.Group;
import com.food.manager.entity.User;
import com.food.manager.mapper.GroupMapper;
import com.food.manager.repository.GroupRepository;
import com.food.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserRepository userRepository;

    public List<GroupResponse> getAllUsers() {
        List<Group> groups = groupRepository.findAll();
        return groupMapper.mapToGroupsList(groups);
    }

    public Optional<GroupResponse> getGroup(Long id) {
        return groupRepository.findById(id).map(groupMapper::toGroupResponse);
    }

    public GroupResponse createGroup(CreateGroupRequest createGroupRequest) {
        Group group = new Group(createGroupRequest.groupName());
        return groupMapper.toGroupResponse(groupRepository.save(group));
    }

    public GroupResponse updateGroup(Long id, UpdateGroupRequest updateGroupRequest) {
        Optional<Group> optionalGroup = groupRepository.findById(id);

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setGroupName(updateGroupRequest.groupName());
            return groupMapper.toGroupResponse(groupRepository.save(group));
        } else
            throw new RuntimeException("Group not found with id: " + id);
    }


    public GroupResponse addUser(AddUserRequest addUserRequest) {
        Optional<Group> groupOptional = groupRepository.findById(addUserRequest.groupId());
        Optional<User> userOptional = userRepository.findById(addUserRequest.userId());

        if (groupOptional.isPresent() && userOptional.isPresent()) {
            Group group = groupOptional.get();
            User user = userOptional.get();

            group.getUsers().add(user);
            user.getGroups().add(group);

            groupRepository.save(group);
            userRepository.save(user);

            return groupMapper.toGroupResponse(group);
        } else {
            throw new RuntimeException("Group or User not found");
        }
    }

    public GroupResponse deleteUser(DeleteUserRequest deleteUserRequest) {
        Optional<Group> groupOptional = groupRepository.findById(deleteUserRequest.userId());
        Optional<User> userOptional = userRepository.findById(deleteUserRequest.userId());

        if (groupOptional.isPresent() && userOptional.isPresent()) {
            Group group = groupOptional.get();
            User user = userOptional.get();

            group.getUsers().remove(user);
            user.getGroups().remove(group);

            return groupMapper.toGroupResponse(group);
        }
        else
            throw new RuntimeException("Group or User not found");
    }

    public void deleteGroup(DeleteGroupRequest deleteGroupRequest) {
        groupRepository.deleteById(deleteGroupRequest.id());
    }
}
