package com.example.demo.controllers;

import com.example.demo.dto.UserDTO;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.RoleService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, RoleService roleService, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userMapper.toUsersDTO(userService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toUserDTO(userService.findById(id)));
    }

    //TODO usunąć - metoda testowa
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }

    //TODO zmienia jedynie id, imie i email
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        userService.updateUser(userDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    //TODO DODAĆ DO SERWISU!!!!!!!!!!!!
    @PatchMapping("/changeRole/{id}")
    public ResponseEntity<MessageResponse> changeRole(@RequestParam ERole newRole, @PathVariable Long id) {
        Role role = roleService.findByName(newRole);
        User user = userService.findById(id);

        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role);

        user.setRoles(newRoles);
        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User role has been changed to: " + newRole));
    }

    @PatchMapping("/addToEvent/{userId}")
    public ResponseEntity<MessageResponse> addToEvent(@RequestParam Long eventId, @PathVariable Long userId) {
        userService.addToEvent(eventId,userId);
        return ResponseEntity.ok(new MessageResponse("User added to event with id: " + eventId));
    }

    @PatchMapping("/leaveFromEvent/{userId}")
    public ResponseEntity<MessageResponse> leaveFromEvent(@RequestParam Long eventId, @PathVariable Long userId) {
        userService.leaveFromEvent(eventId,userId);
        return ResponseEntity.ok(new MessageResponse("User deleted from event with id: " + eventId));
    }
}
