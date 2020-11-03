package com.example.demo.services.implementations;

import com.example.demo.email.EmailService;
import com.example.demo.exceptions.EmailInUseException;
import com.example.demo.exceptions.PeselInUseException;
import com.example.demo.exceptions.RoleNotFoundException;
import com.example.demo.exceptions.UsernameTakenException;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.repository.RoleRepository;
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

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;
    private final EmailService emailService;
    private final PasswordGenerator passwordGenerator;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, EmailService emailService, PasswordGenerator passwordGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.passwordGenerator = passwordGenerator;
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
            throw new UsernameTakenException(signUpRequest.getUsername());
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailInUseException(signUpRequest.getEmail());
        }

        if (userRepository.existsByPesel(signUpRequest.getPesel())) {
            throw new PeselInUseException(signUpRequest.getPesel());
        }

        // TODO generate password
        signUpRequest.setPassword(passwordGenerator.generatePassword(10));

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getFirstName(), signUpRequest
                .getLastName(), signUpRequest.getPesel(), signUpRequest.getEmail(), encoder
                .encode(signUpRequest.getPassword()), true);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                          .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TEACHER));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                                       .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_ADMIN));
                        roles.add(adminRole);

                        break;
                    default:
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                                         .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TEACHER));
                        roles.add(teacherRole);


//                    case "teacher":
//                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
//                                .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TEACHER));
//                        roles.add(teacherRole);
//
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_USER));
//                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        emailService.sendMail(user.getEmail(), "Dane do logowanie w serwisie Domu Kultury",
                "<b>Login: </b> " + signUpRequest.getUsername() + " <br><b>Hasło: </b> " + signUpRequest.getPassword());

        userRepository.save(user);
    }

//    @Override
//    public void registerUser(SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            throw new UsernameTakenException(signUpRequest.getUsername());
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new EmailInUseException(signUpRequest.getEmail());
//        }
//
//        // Create new user's account
//        User user = new User(signUpRequest.getUsername(), signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getPesel(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), false);
//
//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_USER));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_ADMIN));
//                        roles.add(adminRole);
//
//                        break;
//                    case "teacher":
//                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
//                                .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TEACHER));
//                        roles.add(teacherRole);
//
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_USER));
//                        roles.add(userRole);
//                }
//            });
//        }
//
//        user.setRoles(roles);
//        userRepository.save(user);
//    }
}
