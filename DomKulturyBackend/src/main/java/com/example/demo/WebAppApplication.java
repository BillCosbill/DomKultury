package com.example.demo;

import com.example.demo.models.*;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class WebAppApplication {

    RoleRepository roleRepository;
    UserRepository userRepository;
    EventRepository eventRepository;
    RoomRepository roomRepository;

    public WebAppApplication(RoleRepository roleRepository, UserRepository userRepository, EventRepository eventRepository, RoomRepository roomRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.roomRepository = roomRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void fillDB(){
        Room music = new Room("01", "Muzyka", "Sala przeaznaczona do celów muzycznych. Wyposażona w sprzęt do nagrywania oraz instrumenty", 20);
        Room dance = new Room("02", "Taniec", "Sala taneczna o dużej powierzchni i lustrami na ścianach", 10);
        Room it = new Room("03", "Komputery", "Sala komputerowa", 12);

        roomRepository.save(music);
        roomRepository.save(dance);
        roomRepository.save(it);

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

        User userUser2 = new User("user2","user2@onet.pl","$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2"); //pass = 123456
        userUser2.setRoles(userRole);
        userRepository.save(userUser2);

        User userUser3 = new User("user3","user3@onet.pl","$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2"); //pass = 123456
        userUser3.setRoles(userRole);
        userRepository.save(userUser3);

        User teacherUser = new User("teacher","teacher@onet.pl","$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2"); //pass = 123456
        Set<Role> teacherRole = new HashSet<>();
        teacherRole.add(teacher);
        teacherUser.setRoles(teacherRole);
        userRepository.save(teacherUser);

        List<User> studentsList = new ArrayList<>();
        studentsList.add(userUser);
        studentsList.add(userUser2);
        studentsList.add(userUser3);

        Event event = new Event("Muzyka","Lekcje śpiewu", teacherUser, studentsList, 5,
                LocalDateTime.of(2020, Month.SEPTEMBER, 11, 15, 0, 0),
                LocalDateTime.of(2020, Month.SEPTEMBER, 11, 17, 0, 0),
                music);
        eventRepository.save(event);


        List<User> studentsList2 = new ArrayList<>();
        studentsList2.add(userUser);
        studentsList2.add(userUser3);

        Event event2 = new Event("Taniec","Lekcje tańca", teacherUser, studentsList2, 3,
                LocalDateTime.of(2020, Month.SEPTEMBER, 4, 10, 30, 0),
                LocalDateTime.of(2020, Month.SEPTEMBER, 4, 12, 0, 0),
                dance);
        eventRepository.save(event2);

        Event event3 = new Event("Grafika","Kurs grafiki dla dzieci w wieku od 12 do 15 lat", teacherUser, studentsList2, 3,
                LocalDateTime.of(2020, Month.DECEMBER, 5, 12, 30, 0),
                LocalDateTime.of(2020, Month.SEPTEMBER, 4, 15, 0, 0),
                it);
        eventRepository.save(event3);

        Event event4 = new Event("Karate","Zajęcia z karate dla studentów I roku studiów dziennych na Politechnice Białstockiej", teacherUser, studentsList2, 3,
                LocalDateTime.of(2020, Month.NOVEMBER, 11, 10, 30, 0),
                LocalDateTime.of(2020, Month.SEPTEMBER, 4, 12, 0, 0),
                dance);
        eventRepository.save(event4);

    }

}
