package com.example.demo.services.implementations;

import com.example.demo.dto.LessonDTO;
import com.example.demo.exceptions.ConflictGlobalException;
import com.example.demo.exceptions.NotFoundGlobalException;
import com.example.demo.models.Lesson;
import com.example.demo.models.Room;
import com.example.demo.models.Subject;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.LessonService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class LessonServiceImplTest {

    @MockBean
    LessonRepository lessonRepository;

    @MockBean
    RoomRepository roomRepository;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    SubjectRepository subjectRepository;

    @MockBean
    AttendanceRepository attendanceRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LessonService lessonService;

    @BeforeEach
    void init() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("SubjectName");

        Room room = new Room();
        room.setId(1L);
        room.setNumber("01");

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setTopic("Topic1");
        lesson1.setDescription("Description1");
        lesson1.setStartDate(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 0, 0));
        lesson1.setFinishDate(LocalDateTime.of(2020, Month.DECEMBER, 10, 12, 0, 0));
        lesson1.setRoom(room);
        lesson1.setSubject(subject);

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setTopic("Topic2");
        lesson2.setDescription("Description2");
        lesson2.setStartDate(LocalDateTime.of(2020, Month.DECEMBER, 17, 10, 0, 0));
        lesson2.setFinishDate(LocalDateTime.of(2020, Month.DECEMBER, 17, 12, 0, 0));
        lesson2.setRoom(room);
        lesson2.setSubject(subject);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson1));
        when(lessonRepository.findById(2L)).thenReturn(Optional.of(lesson2));
        when(lessonRepository.findAll()).thenReturn(Lists.newArrayList(lesson1, lesson2));
    }

    @Test
    void getLesson_success() {
        Lesson getLesson = lessonService.findById(1L);

        assertEquals(1, getLesson.getId());
        assertEquals("Topic1", getLesson.getTopic());
        assertEquals("Description1", getLesson.getDescription());
        assertEquals(LocalDateTime.of(2020, Month.DECEMBER, 10, 10, 0, 0), getLesson.getStartDate());
        assertEquals(LocalDateTime.of(2020, Month.DECEMBER, 10, 12, 0, 0), getLesson.getFinishDate());
        assertEquals(1, getLesson.getRoom().getId());
        assertEquals(1, getLesson.getSubject().getId());
    }

    @Test
    void getLesson_whenLessonDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> lessonService.findById(3L))
                .hasMessage("Nie znaleziono lekcji z id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void deleteLesson_whenLessonDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> lessonService.deleteLesson(3L))
                .hasMessage("Nie znaleziono lekcji z id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void updateLesson_whenIdConflictDetected_thenThrowException() {
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setId(3L);

        assertThatThrownBy(() -> lessonService.updateLesson(lessonDTO, 4L))
                .hasMessage("Wystapil blad. Id lekcji nie zostalo rozpoznane!")
                .isInstanceOf(ConflictGlobalException.class);
    }
}
