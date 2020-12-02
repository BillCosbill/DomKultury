package com.example.demo.services.implementations;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.exceptions.exception.ConflictGlobalException;
import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.Student;
import com.example.demo.models.Subject;
import com.example.demo.models.User;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.SubjectService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SubjectServiceImplTest {

    @MockBean
    SubjectRepository subjectRepository;

    @MockBean
    LessonRepository lessonRepository;

    @MockBean
    RoomRepository roomRepository;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    AttendanceRepository attendanceRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SubjectService subjectService;

    @BeforeEach
    void init() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        Subject subject1 = new Subject();
        subject1.setId(1L);
        subject1.setName("Name1");
        subject1.setDescription("Description1");
        subject1.setTeacher(user1);

        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setName("Name2");
        subject2.setDescription("Description2");
        subject2.setTeacher(user2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject1));
        when(subjectRepository.findById(2L)).thenReturn(Optional.of(subject2));
        when(subjectRepository.findAll()).thenReturn(Lists.newArrayList(subject1, subject2));
    }

    @Test
    void getLesson_success() {
        Subject getSubject = subjectService.findById(1L);

        assertEquals(1, getSubject.getId());
        assertEquals("Name1", getSubject.getName());
        assertEquals("Description1", getSubject.getDescription());
        assertEquals(1, getSubject.getTeacher().getId());
    }

    @Test
    void getSubject_whenSubjectDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> subjectService.findById(3L))
                .hasMessage("Nie znaleziono przedmiotu o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void deleteSubject_whenSubjectDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> subjectService.deleteSubject(3L))
                .hasMessage("Nie znaleziono przedmiotu o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void updateSubject_whenIdConflictDetected_thenThrowException() {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(3L);

        assertThatThrownBy(() -> subjectService.updateSubject(subjectDTO, 4L))
                .hasMessage("Wystapil blad. Identyfikator przedmiotu nie zostal rozpoznany!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void deleteStudentFromSubject_whenSubjectDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> subjectService.deleteStudentFromSubject(3L, 3L))
                .hasMessage("Nie znaleziono przedmiotu o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void deleteStudentFromSubject_whenStudentDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> subjectService.deleteStudentFromSubject(1L, 3L))
                .hasMessage("Nie znaleziono ucznia o indeksie 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void addStudentToSubject_whenSubjectDoesntExists_thenThrowException() {
        Student student = new Student();
        student.setId(1L);

        assertThatThrownBy(() -> subjectService.addStudentToSubject(student, 3L))
                .hasMessage("Nie znaleziono przedmiotu o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void addStudentFromDatabase_whenStudentDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> subjectService.deleteStudentFromSubject(1L, 3L))
                .hasMessage("Nie znaleziono ucznia o indeksie 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void addStudentFromDatabase_whenSubjectDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> subjectService.deleteStudentFromSubject(3L, 3L))
                .hasMessage("Nie znaleziono przedmiotu o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }
}
