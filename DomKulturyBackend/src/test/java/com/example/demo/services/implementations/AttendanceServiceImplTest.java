package com.example.demo.services.implementations;

import com.example.demo.dto.AttendanceDTO;
import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.Attendance;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.AttendanceService;
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
class AttendanceServiceImplTest {

    @MockBean
    AttendanceRepository attendanceRepository;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    SubjectRepository subjectRepository;

    @MockBean
    LessonRepository lessonRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AttendanceService attendanceService;

    @BeforeEach
    void init() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("Name1");
        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Name2");

        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setTopic("Topic1");

        Attendance attendance1 = new Attendance();
        attendance1.setId(1L);
        attendance1.setStudent(student1);
        attendance1.setLesson(lesson1);
        attendance1.setPresent(true);

        Attendance attendance2 = new Attendance();
        attendance2.setId(2L);
        attendance2.setStudent(student2);
        attendance2.setLesson(lesson1);
        attendance2.setPresent(false);

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance1));
        when(attendanceRepository.findById(2L)).thenReturn(Optional.of(attendance2));
        when(attendanceRepository.findAll()).thenReturn(Lists.newArrayList(attendance1, attendance2));
    }

    @Test
    void getAttendance_success() {
        Attendance getAttendance = attendanceService.findById(1L);

        assertEquals(1L, getAttendance.getId());
        assertEquals(1L, getAttendance.getStudent().getId());
        assertEquals("Name1", getAttendance.getStudent().getFirstName());
        assertEquals(1L, getAttendance.getLesson().getId());
        assertEquals("Topic1", getAttendance.getLesson().getTopic());
        assertTrue(getAttendance.isPresent());
    }

    @Test
    void getAttendance_whenAttendanceDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> attendanceService.findById(3L))
                .hasMessage("Nie znaleziono obiektu frekwencji o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void deleteAttendance_whenAttendanceDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> attendanceService.findById(3L))
                .hasMessage("Nie znaleziono obiektu frekwencji o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void updateAttendance_whenIdConflictDetected_thenThrowException() {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setId(3L);

        assertThatThrownBy(() -> attendanceService.updateAttendance(attendanceDTO, 4L))
                .hasMessage("Wystąpił błąd. Identyfikator obiektu frekwencji nie został rozpoznany!")
                .isInstanceOf(NotFoundGlobalException.class);
    }
}
