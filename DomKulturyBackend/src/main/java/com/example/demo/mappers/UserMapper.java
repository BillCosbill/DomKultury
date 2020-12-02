package com.example.demo.mappers;

import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.Subject;
import com.example.demo.models.User;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public UserMapper(UserRepository userRepository, SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
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
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        userDTO.setEnable(user.isEnable());

        if (user.getSubjects() != null) {
            List<Long> subjectsId = new ArrayList<>();
            user.getSubjects().forEach(subject -> subjectsId.add(subject.getId()));
            userDTO.setSubjectsId(subjectsId);
        }


        return userDTO;
    }

    public User toUser(UserDTO userDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono użytkownika z id " + id));

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
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getSubjectsId() != null) {
            List<Subject> subjects = new ArrayList<>();
            userDTO.getSubjectsId().forEach(subjectId -> subjects.add(subjectRepository.findById(subjectId).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono przedmiotu z id " + subjectId))));
            user.setSubjects(subjects);
        }

        return user;
    }
}
