package com.example.demo.services.implementations;

import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.User;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    //TODO return UserDTO w przyszłości żeby wiedzieć co się usunęło
    @Override
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.findById(user.getId()).map(foundUser -> {
            foundUser.setUsername(user.getUsername());
            foundUser.setPassword(user.getPassword());
            foundUser.setEmail(user.getEmail());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Error: User is not found."));
    }
}
