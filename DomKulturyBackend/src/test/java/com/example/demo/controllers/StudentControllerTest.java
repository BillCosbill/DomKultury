package com.example.demo.controllers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.Student;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.services.implementations.AuthServiceImpl;
import com.example.demo.services.interfaces.AuthService;
import com.example.demo.services.interfaces.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

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
class StudentControllerTest {

    @MockBean
    StudentService studentService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StudentMapper studentMapper;

    @BeforeEach
    void init() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("testLastName1");
        student1.setLastName("testFirstName1");
        student1.setPesel("11111111111");
        student1.setEmail("test1@mail.pl");
        student1.setBirthday(LocalDate.of(1997,Month.SEPTEMBER,26));

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("testLastName2");
        student2.setLastName("testFirstName2");
        student2.setPesel("22222222222");
        student2.setEmail("test2@mail.pl");
        student2.setBirthday(LocalDate.of(1997, Month.SEPTEMBER,27));

        when(studentService.findById(1L)).thenReturn(student1);
        when(studentService.findById(2L)).thenReturn(student2);
        when(studentService.findAll()).thenReturn(Lists.newArrayList(student1, student2));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getStudents() throws Exception {
        mockMvc.perform(get("/students"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].firstName", is("testLastName1")))
               .andExpect(jsonPath("$[0].lastName", is("testFirstName1")))
               .andExpect(jsonPath("$[0].pesel", is("11111111111")))
               .andExpect(jsonPath("$[0].email", is("test1@mail.pl")))
               .andExpect(jsonPath("$[0].birthday", is("1997-09-26")))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].firstName", is("testLastName2")))
               .andExpect(jsonPath("$[1].lastName", is("testFirstName2")))
               .andExpect(jsonPath("$[1].pesel", is("22222222222")))
               .andExpect(jsonPath("$[1].email", is("test2@mail.pl")))
               .andExpect(jsonPath("$[1].birthday", is("1997-09-27")))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getStudent() throws Exception {
        mockMvc.perform(get("/students/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.firstName", is("testLastName1")))
               .andExpect(jsonPath("$.lastName", is("testFirstName1")))
               .andExpect(jsonPath("$.pesel", is("11111111111")))
               .andExpect(jsonPath("$.email", is("test1@mail.pl")))
               .andExpect(jsonPath("$.birthday", is("1997-09-26")))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addStudent() throws Exception {
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setFirstName("test3");
        studentDTO.setLastName("test3");
        studentDTO.setPesel("33333333333");
        studentDTO.setEmail("test3@mail.pl");
        studentDTO.setBirthday("1990-01-01");

        when(studentService.addStudent(any(Student.class))).thenReturn(studentMapper.toStudentAdd(studentDTO));

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/students")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO))
        )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName", is("test3")))
               .andExpect(jsonPath("$.lastName", is("test3")))
               .andExpect(jsonPath("$.pesel", is("33333333333")))
               .andExpect(jsonPath("$.email", is("test3@mail.pl")))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addStudentWithInvalidArguments() throws Exception {
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setFirstName(null);
        studentDTO.setLastName("");
        studentDTO.setPesel("333333333333");
        studentDTO.setEmail("test3mail.pl");
        studentDTO.setBirthday("1990-01-01");

        when(studentService.addStudent(any(Student.class))).thenReturn(studentMapper.toStudentAdd(studentDTO));

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/students")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO))
        )
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStudent() throws Exception {
        mockMvc.perform(delete("/students/{id}", 2))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message", is("Student deleted successfully!")))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteStudentByTeacher_forbidden() throws Exception {
        mockMvc.perform(delete("/students/{id}", 2))
               .andExpect(status().isForbidden())
               .andDo(print());
    }

    @Test
    void deleteStudentByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(delete("/students/{id}", 2))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }
}
