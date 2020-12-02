package com.example.demo.services.implementations;

import com.example.demo.dto.StudentDTO;
import com.example.demo.exceptions.exception.ConflictGlobalException;
import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.Student;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.StudentService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc
class StudentServiceImplTest {

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    SubjectRepository subjectRepository;

    @MockBean
    LessonRepository lessonRepository;

    @MockBean
    AttendanceRepository attendanceRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StudentService studentService;

    @BeforeEach
    void init() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("testFirstName1");
        student1.setLastName("testLastName1");
        student1.setEmail("test1@onet.pl");
        student1.setBirthday(LocalDate.of(1999, Month.JANUARY, 1));

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("testFirstName2");
        student2.setLastName("testLastName2");
        student2.setEmail("test2@onet.pl");
        student2.setBirthday(LocalDate.of(1999, Month.JANUARY, 2));

        Mockito.when(studentRepository.findById(student1.getId())).thenReturn(Optional.of(student1));
        Mockito.when(studentRepository.findById(student2.getId())).thenReturn(Optional.of(student2));

        Mockito.when(studentRepository.findAll()).thenReturn(Lists.newArrayList(student1, student2));
    }

    @Test
    void getStudent_success() {
        Student getStudent = studentService.findById(1L);

        assertEquals(1L, getStudent.getId());
        assertEquals("testFirstName1", getStudent.getFirstName());
        assertEquals("testLastName1", getStudent.getLastName());
        assertEquals("test1@onet.pl", getStudent.getEmail());
        assertEquals(LocalDate.of(1999, Month.JANUARY, 1), getStudent.getBirthday());
    }

    @Test
    void getStudent_whenStudentDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> studentService.findById(3L))
                .hasMessage("Nie znaleziono ucznia o indeksie 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void deleteStudent_whenStudentDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> studentService.deleteStudent(3L))
                .hasMessage("Nie znaleziono ucznia o indeksie 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void updateStudent_whenIdConflictDetected_thenThrowException() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(3L);

        assertThatThrownBy(() -> studentService.updateStudent(studentDTO, 4L))
                .hasMessage("Wystapil blad. Indeks ucznia nie zostal rozpoznany!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void addStudent_whenStudentMailIsTakenByStudent_thenThrowException() {
        Student student = new Student();
        student.setEmail("test@onet.pl");

        Mockito.when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(student))
                .hasMessage("Uczeń z adresem email " + student.getEmail() + " już istnieje!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void addStudent_whenStudentMailIsTakenByUser_thenThrowException() {
        Student student = new Student();
        student.setEmail("test@onet.pl");

        Mockito.when(userRepository.existsByEmail(student.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(student))
                .hasMessage("Użytkownik z adresem email " + student.getEmail() + " już istnieje!")
                .isInstanceOf(ConflictGlobalException.class);
    }
}
