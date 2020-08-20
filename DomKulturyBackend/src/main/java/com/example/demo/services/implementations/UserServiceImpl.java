package com.example.demo.services.implementations;

import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.EventNotFoundException;
import com.example.demo.exceptions.EventStudentsLimitHasBeenReached;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.Event;
import com.example.demo.models.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    EventRepository eventRepository;
    UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
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

        //TODO inny wyjątek, żeby się zgadzało
        if(!userDTO.getId().equals(id)) {
            throw new UserNotFoundException(id);
        }

        User user = userMapper.toUser(userDTO, id);

        return save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void addToEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<User> usersAttended = event.getStudents();

        if (usersAttended.size() >= event.getStudentsLimit()){
            throw new EventStudentsLimitHasBeenReached(eventId);
        } else {
            usersAttended.add(user);
        }


        eventRepository.save(event);
    }

    @Override
    public void leaveFromEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<User> usersAttended = event.getStudents();
        usersAttended.remove(user);

        eventRepository.save(event);
    }
}
