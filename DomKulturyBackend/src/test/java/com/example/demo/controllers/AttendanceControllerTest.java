package com.example.demo.controllers;

import com.example.demo.mappers.AttendanceMapper;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Attendance;
import com.example.demo.models.Lesson;
import com.example.demo.models.Student;
import com.example.demo.services.interfaces.AttendanceService;
import com.example.demo.services.interfaces.StudentService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class AttendanceControllerTest {

    @MockBean
    AttendanceService attendanceService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AttendanceMapper attendanceMapper;

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

        when(attendanceService.findById(1L)).thenReturn(attendance1);
        when(attendanceService.findById(2L)).thenReturn(attendance2);
        when(attendanceService.findAll()).thenReturn(Lists.newArrayList(attendance1, attendance2));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getAttendances() throws Exception {
        mockMvc.perform(get("/attendances"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].lessonId", is(1)))
               .andExpect(jsonPath("$[0].studentId", is(1)))
               .andExpect(jsonPath("$[0].present", is(true)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].lessonId", is(1)))
               .andExpect(jsonPath("$[1].studentId", is(2)))
               .andExpect(jsonPath("$[1].present", is(false)))
               .andDo(print());
    }

    @Test
    void getAttendancesByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(get("/attendances"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getAttendance() throws Exception {
        mockMvc.perform(get("/attendances/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.lessonId", is(1)))
               .andExpect(jsonPath("$.studentId", is(1)))
               .andExpect(jsonPath("$.present", is(true)))
               .andDo(print());
    }

    @Test
    void getAttendanceByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(get("/attendances/1"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }
}
