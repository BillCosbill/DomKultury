package com.example.demo.services.interfaces;

import com.example.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    void deleteUser(Long id);

    User updateUser(User user);
}
