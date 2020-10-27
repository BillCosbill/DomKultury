package com.example.demo.services.interfaces;

import com.example.demo.dto.UserDTO;
import com.example.demo.models.ERole;
import com.example.demo.models.Subject;
import com.example.demo.models.User;
import com.example.demo.payload.request.PasswordChangeRequest;

import java.util.List;

public interface UserService {
    List<User> findAll();

    List<Subject> getUserSubjects(Long userId);

    User findById(Long id);

    void deleteUser(Long id);

    User updateUser(UserDTO userDTO, Long id);

    User save(User user);

    void changeRole(ERole newRole, Long id);

    void changePassword(PasswordChangeRequest passwordChangeRequest, Long id);
}
