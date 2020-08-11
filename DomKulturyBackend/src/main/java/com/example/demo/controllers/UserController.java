package com.example.demo.controllers;

import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*")

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }

    //TODO Jeśli przekażę w ciele użytkownika bez hasła,
    // to w bazie zostanie usunięte hasło użytkownika o wskazanym id
    // Trzeba przekazywać użytkownika ze wszystkimi polami, nie może być pustych pól !!!

    @PutMapping("/users")
    public User updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    //TODO przenieść trzy metody zmiany roli do jednej

    @PatchMapping("/userGiveAdmin")
    public User giveAdminRole(@RequestBody User user){
        Optional<User> userToGetAdmin = userRepository.findById(user.getId());

        if(userToGetAdmin.isPresent()) {
            Optional<Role> newRole = roleRepository.findByName(ERole.ROLE_ADMIN);

            if(newRole.isPresent()){
                Set<Role> newRoles = new HashSet<>();
                newRoles.add(newRole.get());
                userToGetAdmin.get().setRoles(newRoles);

                System.out.println("Nadano rolę administratora użytkownikowi " + user.getUsername());
                return userRepository.save(userToGetAdmin.get());
            } else {
                System.out.println("Nie znaleziono roli ROLE_ADMIN");
                return userRepository.save(user);
            }
        } else {
            System.out.println("Nie znaleziono użytkownika o id " + user.getId());
            return userRepository.save(user);
        }
    }

    @PatchMapping("/userGiveTeacher")
    public User giveTeacherRole(@RequestBody User user){
        Optional<User> userToGetTeacher = userRepository.findById(user.getId());

        if(userToGetTeacher.isPresent()) {
            Optional<Role> newRole = roleRepository.findByName(ERole.ROLE_TEACHER);

            if(newRole.isPresent()){
                Set<Role> newRoles = new HashSet<>();
                newRoles.add(newRole.get());
                userToGetTeacher.get().setRoles(newRoles);

                System.out.println("Nadano rolę użytkownika użytkownikowi " + user.getUsername());
                return userRepository.save(userToGetTeacher.get());
            } else {
                System.out.println("Nie znaleziono roli ROLE_USER");
                return userRepository.save(user);
            }
        } else {
            System.out.println("Nie znaleziono użytkownika o id " + user.getId());
            return userRepository.save(user);
        }
    }

    @PatchMapping("/userGiveUser")
    public User giveUserRole(@RequestBody User user){
        Optional<User> userToGetUser = userRepository.findById(user.getId());

        if(userToGetUser.isPresent()) {
            Optional<Role> newRole = roleRepository.findByName(ERole.ROLE_USER);

            if(newRole.isPresent()){
                Set<Role> newRoles = new HashSet<>();
                newRoles.add(newRole.get());
                userToGetUser.get().setRoles(newRoles);

                System.out.println("Nadano rolę nauczyciela użytkownikowi " + user.getUsername());
                return userRepository.save(userToGetUser.get());
            } else {
                System.out.println("Nie znaleziono roli ROLE_TEACHER");
                return userRepository.save(user);
            }
        } else {
            System.out.println("Nie znaleziono użytkownika o id " + user.getId());
            return userRepository.save(user);
        }
    }


}
