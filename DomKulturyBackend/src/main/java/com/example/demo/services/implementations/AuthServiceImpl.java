package com.example.demo.services.implementations;

import com.example.demo.email.EmailService;
import com.example.demo.exceptions.ConflictGlobalException;
import com.example.demo.exceptions.NotFoundGlobalException;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.userDetails.UserDetailsImpl;
import com.example.demo.services.PasswordGenerator;
import com.example.demo.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final  JwtUtils jwtUtils;
    private final EmailService emailService;
    private final PasswordGenerator passwordGenerator;
    private final StudentRepository studentRepository;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, EmailService emailService, PasswordGenerator passwordGenerator, StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.passwordGenerator = passwordGenerator;
        this.studentRepository = studentRepository;
    }

    @Override
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                                        .map(item -> item.getAuthority())
                                        .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @Override
    public void registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ConflictGlobalException("Użytkownik z nazwą " + signUpRequest.getUsername() + " już istnieje!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ConflictGlobalException("Użytkownik z adresem email " + signUpRequest.getEmail() + " już istnieje!");
        }

        if (userRepository.existsByPesel(signUpRequest.getPesel())) {
            throw new ConflictGlobalException("Użytkownik z peselem " + signUpRequest.getPesel() + " już istnieje!");
        }

        if (studentRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ConflictGlobalException("Uczeń z adresem email " + signUpRequest.getEmail() + " już istnieje!");
        }

        if (studentRepository.existsByPesel(signUpRequest.getPesel())) {
            throw new ConflictGlobalException("Uczeń z peselem  " + signUpRequest.getPesel() + " już istnieje!");

        }

        signUpRequest.setPassword(passwordGenerator.generatePassword(10));

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getFirstName(), signUpRequest
                .getLastName(), signUpRequest.getPesel(), signUpRequest.getEmail(), encoder
                .encode(signUpRequest.getPassword()), true);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                          .orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono roli " + ERole.ROLE_TEACHER));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                                       .orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono roli " + ERole.ROLE_ADMIN));
                        roles.add(adminRole);

                        break;
                    default:
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                                         .orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono roli " + ERole.ROLE_TEACHER));
                        roles.add(teacherRole);
                }
            });
        }

        user.setRoles(roles);

        // TODO fromid uzupełnić
        emailService.registerMailMessage(user.getEmail(), "Dane do logowanie w serwisie Domu Kultury",
                "<b>Login: </b> " + signUpRequest.getUsername() + " <br><b>Hasło: </b> " + signUpRequest.getPassword());

        userRepository.save(user);
    }
}
