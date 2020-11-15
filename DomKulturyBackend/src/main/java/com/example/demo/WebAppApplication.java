package com.example.demo;

import com.example.demo.models.*;
import com.example.demo.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.mail.MessagingException;
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
    public void fillDB() throws MessagingException {
        Room classComputer = new Room("01", "Sala komputerowa", "Sala komputerowa\n" +
                "\n" +
                "Elegancka, nowoczesna przystosowana do profesjonalnych szkoleń, sala z dostępem do 15 stacjonarnych stanowisk komputerowych. Pomieszczenie klimatyzowane, dobrze nasłonecznione z możliwością zaciemnienia (rolety).\n" +
                "\n" +
                "Wyposażenie:\n" +
                "- projektor multimedialny,\n" +
                "- ekran,\n" +
                "- tablica suchościeralna,\n" +
                "- stałe łącze internetowe");
        Room classGym = new Room("02", "Sala gimnastyczna", "Miejsce przeznaczone do uprawiania sportu. Jej przeznaczenie może być dostosowane do prowadzenia grupowych zajęć ruchowych dla dzieci oraz dorosłych.\n" +
                "\n" +
                "Wyposażenie:\n" +
                "- piłki do gry w piłkę nożną,\n" +
                "- piłki siatkowe,\n" +
                "- piłki do kosza,\n" +
                "- 2 bramki,\n" +
                "- kosze do gry w koszykówkę,\n" +
                "- składana siatka do siatkówki");
        Room classLesson = new Room("03", "Sala lekcyjna", "Sala przeznaczona do prowadzenia zajęć ogólnokształcących na których uczeń nie potrzebuje specjalistycznego sprzętu, a jedynie miejsca.\n" +
                "\n" +
                "Wyposażenie:\n" +
                "- 20 + 1 ławek\n" +
                "- 40 + 1 krzeseł\n" +
                "- tablica suchościeralna\n" +
                "- wyświetlacz multimedialny");

        roomRepository.save(classComputer);
        roomRepository.save(classGym);
        roomRepository.save(classLesson);

//        Role user = new Role(ERole.ROLE_USER);
        Role teacher = new Role(ERole.ROLE_TEACHER);
        Role admin = new Role(ERole.ROLE_ADMIN);

//        roleRepository.save(user);
        roleRepository.save(teacher);
        roleRepository.save(admin);

        Student student1 = new Student("Mieczysław", "Boczek", "m.boczek@onet.pl", LocalDate
                .of(1997, 9, 26));
        Student student2 = new Student("Katarzyna", "Pączek", "k.paczek@onet.pl", LocalDate
                .of(1999, 6, 27));
        Student student3 = new Student("Andrzej", "Wielki", "a.wielki@onet.pl", LocalDate
                .of(1998, 2, 22));
        Student student4 = new Student("Agnieszka", "Niemieszkalska", "a.niemieszkalska@onet.pl", LocalDate
                .of(1997, 10, 10));
        Student student5 = new Student("Tomasz", "Mleczak", "t.szeligowski26@gmail.com", LocalDate
                .of(1997, 10, 10));

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        User adminUser = new User("admin", "Tomasz", "Szeligowski", "t.szeligowski@onet.pl", "$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", true); //pass = 123456
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(admin);
        adminUser.setRoles(adminRole);
        userRepository.save(adminUser);

        User teacherUser = new User("teacher", "Jan", "Kowalski", "teacher@gmail.com", "$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", true); //pass = 123456
        Set<Role> teacherRole = new HashSet<>();
        teacherRole.add(teacher);
        teacherUser.setRoles(teacherRole);
        userRepository.save(teacherUser);

        Subject subject1 = new Subject("Programowanie w języku Java", "Na kursie nauczysz się języka Java do zastosowań w Internecie od podstaw. Jest to uniwersalny język, w którym tworzy się zarówno aplikacje mobilne jak i rozbudowane systemy bankowe. Poznasz Spring - jeden z najważniejszych frameworków dla Javy oraz bibliotekę Hibernate. \n" +
                "\n" +
                "Do kursu możesz przystąpić, nawet jeśli nie masz doświadczenia w programowaniu ani wykształcenia w podobnym kierunku - choć są to mile widziane atuty. Java jest dla Ciebie, jeśli lubisz zagadki logiczne, dokładnie analizujesz problemy i jesteś systematyczny.", adminUser, students);

        subjectRepository.save(subject1);

        Lesson lesson1 = new Lesson("Wstęp do języka Java", "W ramach wprowadzenia już w&nbsp;pierwszych dniach kursu&nbsp;napiszesz swoje pierwsze programy. Dzięki temu sprawnie zrozumiesz działanie języka Java. Dowiesz się jak za jego pomocą tworzyć programy konsolowe. Nauczysz się też debugowania - dzięki niemu wykryjesz błędy w swojej aplikacji",
                LocalDateTime.of(2020, Month.NOVEMBER, 2, 15, 0, 0),
                LocalDateTime.of(2020, Month.NOVEMBER, 2, 18, 0, 0),
                classComputer, subject1);

        Lesson lesson2 = new Lesson("Programowanie obiektowe i bazy danych", "Po pierwszym bloku znasz już podstawy, dlatego w drugim nauczysz się programowania obiektowego. Jest to technika podziału kodu, dzięki której będziesz miał&nbsp;lepszą kontrolę nad swoim projektem. Poznasz też SQL - czyli język do komunikacji z bazami danych. Zakończeniem bloku będą warsztaty, na których stworzysz konsolową&nbsp;wersję Twittera",
                LocalDateTime.of(2020, Month.NOVEMBER, 9, 15, 0, 0),
                LocalDateTime.of(2020, Month.NOVEMBER, 9, 18, 0, 0),
                classComputer, subject1);

        Lesson lesson3 = new Lesson("HTTP i aplikacje webowe", "Skoro wiesz już jak uporządkować swój projekt, to zrobimy krok dalej. Dowiesz się czym jest protokół HTTP i jak możesz go wykorzystać oraz do czego służą żądania POST i GET. Ponadto nauczysz się tworzyć aplikacje webowe i poruszymy bardzo ważne zagadnienie - jak właściwie testować swoją&nbsp;aplikację",
                LocalDateTime.of(2020, Month.NOVEMBER, 16, 15, 0, 0),
                LocalDateTime.of(2020, Month.NOVEMBER, 16, 18, 0, 0),
                classComputer, subject1);

        Lesson lesson4 = new Lesson("Javascript, czyli wprowadzamy dynamikę", "W bloku czwartym czas nadać Twoim projektom nieco życia. W tym celu poznasz i opanujesz język JavaScript. Poznasz też zbiór wytycznych i reguł REST, według którego powinieneś projektować swoje aplikacje. To taki kodeks dobrego programisty",
                LocalDateTime.of(2020, Month.NOVEMBER, 23, 15, 0, 0),
                LocalDateTime.of(2020, Month.NOVEMBER, 23, 18, 0, 0),
                classComputer, subject1);

        Lesson lesson5 = new Lesson("Zaawansowana Java", "W piątym bloku pogłębisz swoją wiedzę o Javie. Sięgniemy do zaawansowanych funkcji tego języka, które pozwalają na pisanie rozbudowanych aplikacji. Poznasz interfejsy, adnotacje oraz wzorce projektowe.",
                LocalDateTime.of(2020, Month.NOVEMBER, 30, 15, 0, 0),
                LocalDateTime.of(2020, Month.NOVEMBER, 30, 18, 0, 0),
                classComputer, subject1);

        Lesson lesson6 = new Lesson("Framework Spring", "Spring to jeden z najważniejszych frameworków dla języka Java. Przyjrzymy mu się w 6 bloku - nauczysz się pisać w nim aplikacje. Ponadto poznasz znaczenie i zastosowanie tajemniczych pojęć, takich jak MVC lub Dependency Injection",
                LocalDateTime.of(2020, Month.DECEMBER, 7, 15, 0, 0),
                LocalDateTime.of(2020, Month.DECEMBER, 7, 18, 0, 0),
                classComputer, subject1);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        lessonRepository.save(lesson3);
        lessonRepository.save(lesson4);
        lessonRepository.save(lesson5);
        lessonRepository.save(lesson6);

        Attendance attendance1 = new Attendance(lesson1, student1, true);
        Attendance attendance2 = new Attendance(lesson1, student2, true);
        Attendance attendance3 = new Attendance(lesson1, student3, true);
        Attendance attendance4 = new Attendance(lesson1, student4, true);
        Attendance attendance5 = new Attendance(lesson1, student5, true);

        Attendance attendance6 = new Attendance(lesson2, student1, true);
        Attendance attendance7 = new Attendance(lesson2, student2, false);
        Attendance attendance8 = new Attendance(lesson2, student3, true);
        Attendance attendance9 = new Attendance(lesson2, student4, false);
        Attendance attendance10 = new Attendance(lesson2, student5, true);

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);
        attendanceRepository.save(attendance3);
        attendanceRepository.save(attendance4);
        attendanceRepository.save(attendance5);
        attendanceRepository.save(attendance6);
        attendanceRepository.save(attendance7);
        attendanceRepository.save(attendance8);
        attendanceRepository.save(attendance9);
        attendanceRepository.save(attendance10);
    }

}
