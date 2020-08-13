package com.example.demo.mappers;

import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    UserRepository userRepository;

    @Autowired
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> toUsersDTO(List<User> users) {
        return ((List<UserDTO>) users.stream().map(this::toUserDTO).collect(Collectors.toList()));
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new UserNotFoundException(userDTO.getId()));

        if(userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }
        if(userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if(userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if(userDTO.getRoles() != null) {
            user.setRoles(userDTO.getRoles());
        }

        return user;
    }
}
