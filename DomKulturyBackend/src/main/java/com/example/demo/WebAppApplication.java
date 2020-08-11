package com.example.demo;

import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class WebAppApplication {

    RoleRepository roleRepository;
    UserRepository userRepository;

    public WebAppApplication(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void fillDB(){
        Role user = new Role(ERole.ROLE_USER);
        Role teacher = new Role(ERole.ROLE_TEACHER);
        Role admin = new Role(ERole.ROLE_ADMIN);

        roleRepository.save(user);
        roleRepository.save(teacher);
        roleRepository.save(admin);

        User adminUser = new User("admin","admin@onet.pl","$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2"); //pass = 123456
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(admin);
        adminUser.setRoles(adminRole);
        userRepository.save(adminUser);

        User userUser = new User("user","user@onet.pl","$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2"); //pass = 123456
        Set<Role> userRole = new HashSet<>();
        userRole.add(user);
        userUser.setRoles(userRole);
        userRepository.save(userUser);

        User teacherUser = new User("teacher","teacher@onet.pl","$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2"); //pass = 123456
        Set<Role> teacherRole = new HashSet<>();
        teacherRole.add(teacher);
        teacherUser.setRoles(teacherRole);
        userRepository.save(teacherUser);
    }

}
