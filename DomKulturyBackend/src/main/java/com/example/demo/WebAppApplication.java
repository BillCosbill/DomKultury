package com.example.demo;

import com.example.demo.models.*;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.AttendanceService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
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
    RoomRepository roomRepository;
    StudentRepository studentRepository;
    SubjectRepository subjectRepository;
    LessonRepository lessonRepository;
    AttendanceRepository attendanceRepository;

    public WebAppApplication(RoleRepository roleRepository, UserRepository userRepository, RoomRepository roomRepository,
                             StudentRepository studentRepository, SubjectRepository subjectRepository, LessonRepository lessonRepository, AttendanceRepository attendanceRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.lessonRepository = lessonRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
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

        Student student1 = new Student("Tomasz", "Szeligowski", "97092606913", "t.szeligowski@onet.pl", LocalDate
                .of(1997, 9, 26));
        Student student2 = new Student("Aleksandra", "Toczko", "99062713215", "a.toczko@onet.pl", LocalDate
                .of(1999, 6, 27));
        Student student3 = new Student("Andrzej", "Duda", "98022224156", "a.duda@onet.pl", LocalDate.of(1998, 2, 22));
        Student student4 = new Student("Agnieszka", "Niemieszkalska", "97101023162", "a.niemieszkalska@onet.pl", LocalDate
                .of(1997, 10, 10));

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);

        List<Student> studentsList1 = new ArrayList<>();
        studentsList1.add(student2);
        studentsList1.add(student3);

        List<Student> studentsList2 = new ArrayList<>();
        studentsList2.add(student1);
        studentsList2.add(student4);

        User adminUser = new User("admin","Tomasz","Szeligowski","97092606913", "admin@onet.pl", "$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", true); //pass = 123456
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(admin);
        adminUser.setRoles(adminRole);
        userRepository.save(adminUser);

        User userUser = new User("user","Jan","Kowalski","11111111111", "user@onet.pl", "$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", true); //pass = 123456
        Set<Role> userRole = new HashSet<>();
        userRole.add(user);
        userUser.setRoles(userRole);
        userRepository.save(userUser);

        User userUser2 = new User("user2","Ola","Nowak","22222222222", "user2@onet.pl", "$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", true); //pass = 123456
        userUser2.setRoles(userRole);
        userRepository.save(userUser2);

        User userUser3 = new User("user3","Jerzy","Dudek","33333333333", "user3@onet.pl", "$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", true); //pass = 123456
        userUser3.setRoles(userRole);
        userRepository.save(userUser3);

        User teacherUser = new User("teacher","Andrzej","Samczy","44444444444", "teacher@onet.pl", "$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", true); //pass = 123456
        Set<Role> teacherRole = new HashSet<>();
        teacherRole.add(teacher);
        teacherUser.setRoles(teacherRole);
        userRepository.save(teacherUser);

        List<User> studentsList = new ArrayList<>();
        studentsList.add(userUser);
        studentsList.add(userUser2);
        studentsList.add(userUser3);

        Subject subject1 = new Subject("Programowanie w językui Java", "Celem przedmiotu jest poznanie przez uczestników zajęć podstaw języka Java, które pozwolą im na samodzielne tworzenie prostych aplikacji okienkowych", teacherUser, studentsList1);
        Subject subject2 = new Subject("Angielski", "Kurs angielskiego na poziomie B2, którego celem jest przygotowanie do egzaminu maturalnego", adminUser, studentsList2);

        subjectRepository.save(subject1);
        subjectRepository.save(subject2);

        Lesson lesson1 = new Lesson("Funkcje w Java","Uczeń poznaje funkcje w języku java",
                LocalDateTime.of(2020, Month.SEPTEMBER, 11, 15, 0, 0),
                LocalDateTime.of(2020, Month.SEPTEMBER, 11, 17, 0, 0),
                it, subject1);

        Lesson lesson2 = new Lesson("JavaFX","Uczeń uczy się tworzyć aplikacje okienkowe",
                LocalDateTime.of(2020, Month.SEPTEMBER, 18, 15, 0, 0),
                LocalDateTime.of(2020, Month.SEPTEMBER, 18, 17, 0, 0),
                it, subject1);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);

        Attendance attendance1 = new Attendance(lesson1, student2);
        Attendance attendance2 = new Attendance(lesson1, student3);

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

        Attendance attendance3 = new Attendance(lesson2, student2);
        Attendance attendance4 = new Attendance(lesson2, student3);

        attendanceRepository.save(attendance3);
        attendanceRepository.save(attendance4);
    }

}
