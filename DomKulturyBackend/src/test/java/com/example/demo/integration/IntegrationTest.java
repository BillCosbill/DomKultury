package com.example.demo.integration;

import com.example.demo.models.*;
import com.example.demo.repository.RoleRepository;
import com.example.demo.services.interfaces.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class IntegrationTest {

    @Autowired
    LessonService lessonService;

    @Autowired
    RoomService roomService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    StudentService studentService;

    @Test
    void testDatabaseIntegration(){
        int countLessons = lessonService.findAll().size();
        assertEquals(countLessons, lessonService.findAll().size());

        int countRooms = roomService.findAll().size();
        assertEquals(countRooms, roomService.findAll().size());

        int countStudents = studentService.findAll().size();
        assertEquals(countStudents, studentService.findAll().size());

        int countSubjects = subjectService.findAll().size();
        assertEquals(countSubjects, subjectService.findAll().size());

        int countUsers = userService.findAll().size();
        assertEquals(countUsers, userService.findAll().size());

        int countRoles = roleRepository.findAll().size();
        assertEquals(countRoles, roleRepository.findAll().size());

        Student student = new Student();
        student.setFirstName("TestName");
        student.setLastName("TestLastName");
        student.setEmail("test@onet.pl");
        student.setBirthday(LocalDate.of(1997, Month.SEPTEMBER, 11));

        Long newStudentId = studentService.addStudent(student).getId();
        assertEquals(countStudents + 1, studentService.findAll().size());

        Room room = new Room();
        room.setNumber("TestNumber");
        room.setDescription("TestDescription");
        room.setDestiny("TestDestiny");

        Long newRoomId = roomService.addRoom(room, null).getId();
        assertEquals(countRooms + 1, roomService.findAll().size());

        Role role = new Role(ERole.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Integer newRoleId = roleRepository.save(role).getId();
        assertEquals(countRoles + 1, roleRepository.findAll().size());

        User user = new User();
        user.setUsername("testUsername");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user.setEmail("test@onet.pl");
        user.setRoles(roles);
        user.setPassword("$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2");
        user.setEnable(true);

        Long newUserId = userService.save(user).getId();
        assertEquals(countUsers + 1, userService.findAll().size());

        Subject subject = new Subject();
        subject.setName("TestName");
        subject.setDescription("TestDescription");
        subject.setTeacher(user);

        Long newSubjectId = subjectService.addSubject(subject).getId();
        assertEquals(countSubjects + 1, subjectService.findAll().size());

        Lesson lesson = new Lesson();
        lesson.setTopic("TestTopic");
        lesson.setDescription("TestDescription");
        lesson.setStartDate(LocalDateTime.of(2020, Month.SEPTEMBER,20,10,0));
        lesson.setFinishDate(LocalDateTime.of(2020, Month.SEPTEMBER,20,12,0));
        lesson.setRoom(room);
        lesson.setSubject(subject);

        Long newLessonId = lessonService.save(lesson).getId();
        assertEquals(countLessons + 1, lessonService.findAll().size());

        Student newStudent = studentService.findById(newStudentId);
        assertEquals(countStudents + 1, newStudentId);
        assertEquals("TestName", newStudent.getFirstName());
        assertEquals("TestLastName", newStudent.getLastName());
        assertEquals("test@onet.pl", newStudent.getEmail());
        assertEquals("1997-09-11", newStudent.getBirthday().toString());

        Lesson newLesson = lessonService.findById(newLessonId);
        assertEquals(countLessons + 1, newLessonId);
        assertEquals("TestTopic", newLesson.getTopic());
        assertEquals("TestDescription", newLesson.getDescription());
        assertEquals("2020-09-20T10:00", newLesson.getStartDate().toString());
        assertEquals("2020-09-20T12:00", newLesson.getFinishDate().toString());

        Room newRoom = roomService.findById(newRoomId);
        assertEquals(countRooms + 1, newRoomId);
        assertEquals("TestNumber", newRoom.getNumber());
        assertEquals("TestDescription", newRoom.getDescription());
        assertEquals("TestDestiny", newRoom.getDestiny());

        Subject newSubject = subjectService.findById(newSubjectId);
        assertEquals(countSubjects + 1, newSubjectId);
        assertEquals("TestName", newSubject.getName());
        assertEquals("TestDescription", newSubject.getDescription());
        assertEquals("test@onet.pl", newSubject.getTeacher().getEmail());

        User newUser = userService.findById(newUserId);

        assertEquals(newUserId, newUser.getId());
        assertEquals("testUsername", newUser.getUsername());
        assertEquals("testFirstname", newUser.getFirstName());
        assertEquals("testLastname", newUser.getLastName());
        assertEquals("test@onet.pl", newUser.getEmail());
        assertEquals("$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2", newUser.getPassword());
        assertTrue(newUser.isEnable());
    }

}
