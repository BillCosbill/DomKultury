package com.example.demo.controllers;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.EmailInUseException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.mappers.SubjectMapper;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.payload.request.PasswordChangeRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.services.interfaces.RoleService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final SubjectMapper subjectMapper;

    @Autowired
    public UserController(UserService userService, RoleService roleService, UserMapper userMapper, SubjectMapper subjectMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.subjectMapper = subjectMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userMapper.toUsersDTO(userService.findAll()));
    }

    @GetMapping("/{userId}/subjects")
    public ResponseEntity<List<SubjectDTO>> getUserSubjects(@PathVariable Long userId) {
        return ResponseEntity.ok(subjectMapper.toSubjectsDTO(userService.getUserSubjects(userId)));
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

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        userService.updateUser(userDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PatchMapping("/changeRole/{id}")
    public ResponseEntity<MessageResponse> changeRole(@RequestParam ERole newRole, @PathVariable Long id) {
        userService.changeRole(newRole, id);

        return ResponseEntity.ok(new MessageResponse("User role has been changed to: " + newRole));
    }

    @PatchMapping("/changePassword/{id}")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, @PathVariable Long id) {
        userService.changePassword(passwordChangeRequest, id);

        return ResponseEntity.ok(new MessageResponse("User password has been changed"));
    }
}
