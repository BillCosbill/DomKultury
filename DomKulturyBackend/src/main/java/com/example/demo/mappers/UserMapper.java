package com.example.demo.mappers;

import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.EventNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    UserRepository userRepository;
    EventRepository eventRepository;

    @Autowired
    public UserMapper(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public List<UserDTO> toUsersDTO(List<User> users) {
        return ((List<UserDTO>) users.stream().map(this::toUserDTO).collect(Collectors.toList()));
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPesel(user.getPesel());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        List<Long> conductedId = new ArrayList<>();
        user.getClassConducted().forEach(x -> conductedId.add(x.getId()));
        userDTO.setClassConductedId(conductedId);

        List<Long> attendedId = new ArrayList<>();
        user.getClassAttended().forEach(x -> attendedId.add(x.getId()));
        userDTO.setClassAttendedId(attendedId);

        userDTO.setEnable(user.isEnable());

        return userDTO;
    }

    public User toUser(UserDTO userDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getPesel() != null) {
            user.setPesel(userDTO.getPesel());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        return user;
    }
}
