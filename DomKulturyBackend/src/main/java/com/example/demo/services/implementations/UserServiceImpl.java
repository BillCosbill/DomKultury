package com.example.demo.services.implementations;

import com.example.demo.dto.UserDTO;
import com.example.demo.email.EmailService;
import com.example.demo.exceptions.*;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.Subject;
import com.example.demo.models.User;
import com.example.demo.payload.request.PasswordChangeRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.PasswordGenerator;
import com.example.demo.services.interfaces.RoleService;
import com.example.demo.services.interfaces.SubjectService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final SubjectService subjectService;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleService roleService, PasswordEncoder encoder, AuthenticationManager authenticationManager, SubjectService subjectService, PasswordGenerator passwordGenerator, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.subjectService = subjectService;
        this.passwordGenerator = passwordGenerator;
        this.emailService = emailService;
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
        return userRepository.findById(id).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono użytkownika o id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        User user = findById(id);

        if (subjectService.findAll().stream().filter(subject -> subject.getTeacher().getId() == id).findAny().isPresent()) {
            throw new ConflictGlobalException("Użytkownik o id " + id + " jest przypisany do co najmniej jednego przedmiotu!");
        }

        userRepository.delete(user);
    }

    @Override
    public User updateUser(UserDTO userDTO, Long id) {

        User user = findById(id);

        if(!userDTO.getId().equals(id)) {
            throw new NotFoundGlobalException("Nie znaleziono użytkownika o id " + id);
        }

        if(!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ConflictGlobalException("Użytkownik z adresm email " + userDTO.getEmail() + " już istnieje!");
        }

        if(!user.getUsername().equals(userDTO.getUsername()) && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ConflictGlobalException("Użytkownik z nazwą " + userDTO.getUsername() + " już istnieje!");
        }

        if(!user.getPesel().equals(userDTO.getPesel()) && userRepository.existsByPesel(userDTO.getPesel())) {
            throw new ConflictGlobalException("Użytkownik z peselem " + userDTO.getPesel() + " już istnieje!");
        }

        user = userMapper.toUser(userDTO, id);

        return save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void changeRole(ERole newRole, Long id) {
        Role role = roleService.findByName(newRole);
        User user = findById(id);

        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role);

        user.setRoles(newRoles);
        save(user);
    }

    @Override
    public void changePassword(PasswordChangeRequest passwordChangeRequest, Long id) {
        User user = findById(id);

        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), passwordChangeRequest.getPassword()));
        } catch (Exception e) {
            throw new ConflictGlobalException("Wprowadzono błędne hasło!");
        }


        user.setPassword(encoder.encode(passwordChangeRequest.getNewPassword()));
        save(user);
    }

    @Override
    public void generateNewPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            String newPassword = passwordGenerator.generatePassword(10);
            user.setPassword(encoder.encode(newPassword));

            // TODO fromid uzupełnić
            emailService.sendMail(null, user.getEmail(), "Twoje nowe dane do logowania w narzędziu naszego Domu Kultury",
                    "<b>Login: </b> " + user.getUsername() + " <br><b>Hasło: </b> " + newPassword);

            userRepository.save(user);
        }
    }


}
