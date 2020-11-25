package com.example.demo.controllers;

import com.example.demo.dto.LessonDTO;
import com.example.demo.dto.StudentDTO;
import com.example.demo.mappers.LessonMapper;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Lesson;
import com.example.demo.models.Room;
import com.example.demo.models.Student;
import com.example.demo.models.Subject;
import com.example.demo.services.interfaces.LessonService;
import com.example.demo.services.interfaces.RoomService;
import com.example.demo.services.interfaces.StudentService;
import com.example.demo.services.interfaces.SubjectService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class LessonControllerTest {

    @MockBean
    LessonService lessonService;

    @MockBean
    SubjectService subjectService;

    @MockBean
    RoomService roomService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LessonMapper lessonMapper;

    @BeforeEach
    void init() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("SubjectName");

        Room room = new Room();
        room.setId(1L);
        room.setNumber("01");

        when(subjectService.findById(1L)).thenReturn(subject);
        when(roomService.findById(1L)).thenReturn(room);

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

        when(lessonService.findById(1L)).thenReturn(lesson1);
        when(lessonService.findById(2L)).thenReturn(lesson2);
        when(lessonService.findAll()).thenReturn(Lists.newArrayList(lesson1, lesson2));
    }

    @Test
    void getLessons() throws Exception {
        mockMvc.perform(get("/lessons"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].topic", is("Topic1")))
               .andExpect(jsonPath("$[0].description", is("Description1")))
               .andExpect(jsonPath("$[0].startDate", is("2020-12-10T10:00")))
               .andExpect(jsonPath("$[0].finishDate", is("2020-12-10T12:00")))
               .andExpect(jsonPath("$[0].roomId", is(1)))
               .andExpect(jsonPath("$[0].subjectId", is(1)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].topic", is("Topic2")))
               .andExpect(jsonPath("$[1].description", is("Description2")))
               .andExpect(jsonPath("$[1].startDate", is("2020-12-17T10:00")))
               .andExpect(jsonPath("$[1].finishDate", is("2020-12-17T12:00")))
               .andExpect(jsonPath("$[1].roomId", is(1)))
               .andExpect(jsonPath("$[1].subjectId", is(1)))
               .andDo(print());
    }

    @Test
    void getLesson() throws Exception {
        mockMvc.perform(get("/lessons/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.topic", is("Topic1")))
               .andExpect(jsonPath("$.description", is("Description1")))
               .andExpect(jsonPath("$.startDate", is("2020-12-10T10:00")))
               .andExpect(jsonPath("$.finishDate", is("2020-12-10T12:00")))
               .andExpect(jsonPath("$.roomId", is(1)))
               .andExpect(jsonPath("$.subjectId", is(1)))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getStudentAttendance() throws Exception {
        mockMvc.perform(get("/lessons/1/studentAttendances"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void getStudentAttendanceByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(get("/lessons/1/studentAttendances"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }


    @Test
    @WithMockUser(roles = "TEACHER")
    void addLesson() throws Exception {
        when(lessonService.addLesson(any(Lesson.class))).thenAnswer(invocationOnMock -> {
            Lesson lessonOnMock = invocationOnMock.getArgument(0);

            assertNull(lessonOnMock.getId());
            assertEquals("Topic1", lessonOnMock.getTopic());
            assertEquals("Description1", lessonOnMock.getDescription());
            assertEquals("2020-12-10T10:00", lessonOnMock.getStartDate().toString());
            assertEquals("2020-12-10T12:00", lessonOnMock.getFinishDate().toString());
            assertEquals(1, lessonOnMock.getRoom().getId());
            assertEquals(1, lessonOnMock.getSubject().getId());

            Lesson newLesson = new Lesson();
            newLesson.setTopic(lessonOnMock.getTopic());
            newLesson.setDescription(lessonOnMock.getDescription());
            newLesson.setStartDate(lessonOnMock.getStartDate());
            newLesson.setFinishDate(lessonOnMock.getFinishDate());
            newLesson.setRoom(roomService.findById(lessonOnMock.getRoom().getId()));
            newLesson.setSubject(subjectService.findById(lessonOnMock.getSubject().getId()));

            return newLesson;
        });

        mockMvc.perform(post("/lessons")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"topic\":\"Topic1\",\"description\":\"Description1\",\"startDate\":\"2020-12-10T10:00\",\"finishDate\":\"2020-12-10T12:00\",\"roomId\":\"1\",\"subjectId\":\"1\"}")
        )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.topic", is("Topic1")))
               .andExpect(jsonPath("$.description", is("Description1")))
               .andExpect(jsonPath("$.startDate", is("2020-12-10T10:00")))
               .andExpect(jsonPath("$.finishDate", is("2020-12-10T12:00")))
               .andExpect(jsonPath("$.roomId", is(1)))
               .andExpect(jsonPath("$.subjectId", is(1)))
               .andDo(print());
    }

    @Test
    void addLessonByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(post("/lessons"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteLesson() throws Exception {
        mockMvc.perform(delete("/lessons/{id}", 2))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message", is("Lekcja o id 2 została usunięta!")))
               .andDo(print());
    }

    @Test
    void deleteLessonByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(delete("/lessons/{id}", 2))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void updateLesson() throws Exception {
        when(lessonService.updateLesson(any(LessonDTO.class), any(Long.class))).thenAnswer(invocationOnMock -> {
            LessonDTO lessonOnMock = invocationOnMock.getArgument(0);
            Long lessonIdOnMock = invocationOnMock.getArgument(1);

            assertEquals(1, lessonOnMock.getId());
            assertEquals("Topic1new", lessonOnMock.getTopic());
            assertEquals("Description1new", lessonOnMock.getDescription());
            assertEquals("2020-12-10T10:00", lessonOnMock.getStartDate());
            assertEquals("2020-12-10T12:00", lessonOnMock.getFinishDate());
            assertEquals(1, lessonOnMock.getRoomId());
            assertEquals(1, lessonOnMock.getSubjectId());

            assertEquals(1L, lessonIdOnMock);

            Lesson newLesson = new Lesson();
            newLesson.setId(lessonOnMock.getId());
            newLesson.setTopic(lessonOnMock.getTopic());
            newLesson.setDescription(lessonOnMock.getDescription());
            newLesson.setStartDate(LocalDateTime.parse(lessonOnMock.getStartDate()));
            newLesson.setFinishDate(LocalDateTime.parse(lessonOnMock.getFinishDate()));
            newLesson.setRoom(roomService.findById(lessonOnMock.getRoomId()));
            newLesson.setSubject(subjectService.findById(lessonOnMock.getSubjectId()));

            return newLesson;
        });

        mockMvc.perform(put("/lessons/" + "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"topic\":\"Topic1new\",\"description\":\"Description1new\",\"startDate\":\"2020-12-10T10:00\",\"finishDate\":\"2020-12-10T12:00\",\"roomId\":\"1\",\"subjectId\":\"1\"}")
        )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.topic", is("Topic1new")))
               .andExpect(jsonPath("$.description", is("Description1new")))
               .andExpect(jsonPath("$.startDate", is("2020-12-10T10:00")))
               .andExpect(jsonPath("$.finishDate", is("2020-12-10T12:00")))
               .andExpect(jsonPath("$.roomId", is(1)))
               .andExpect(jsonPath("$.subjectId", is(1)))
               .andDo(print());
    }

    @Test
    void updateLessonByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(put("/lessons/" + "1"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

}
