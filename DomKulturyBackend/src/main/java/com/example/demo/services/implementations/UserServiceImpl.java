package com.example.demo.services.implementations;

import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.EmailInUseException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.exceptions.UsernameTakenException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.Subject;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.interfaces.SubjectService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Subject> getUserSubjects(Long userId) {
        return findById(userId).getSubjects();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Override
    public User updateUser(UserDTO userDTO, Long id) {

        User user = findById(id);

        //TODO inny wyjątek, żeby się zgadzało
        if(!userDTO.getId().equals(id)) {
            throw new UserNotFoundException(id);
        }

        if(!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailInUseException(userDTO.getEmail());
        }

        if(!user.getUsername().equals(userDTO.getUsername()) && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameTakenException(userDTO.getUsername());
        }

        if(!user.getPesel().equals(userDTO.getPesel()) && userRepository.existsByPesel(userDTO.getPesel())) {
            throw new EmailInUseException(userDTO.getPesel());
        }

        user = userMapper.toUser(userDTO, id);

        return save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
