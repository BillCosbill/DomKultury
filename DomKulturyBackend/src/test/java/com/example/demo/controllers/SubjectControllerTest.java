package com.example.demo.controllers;

import com.example.demo.dto.SubjectDTO;
import com.example.demo.mappers.SubjectMapper;
import com.example.demo.models.Subject;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.StudentService;
import com.example.demo.services.interfaces.SubjectService;
import com.example.demo.services.interfaces.UserService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class SubjectControllerTest {
    @MockBean
    SubjectService subjectService;

    @MockBean
    UserService userService;

    @MockBean
    StudentService studentService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SubjectMapper subjectMapper;

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

        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        when(subjectService.findById(1L)).thenReturn(subject1);
        when(subjectService.findById(2L)).thenReturn(subject2);
        when(subjectService.findAll()).thenReturn(Lists.newArrayList(subject1, subject2));
    }

    @Test
    void getSubjects() throws Exception {
        mockMvc.perform(get("/subjects"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].name", is("Name1")))
               .andExpect(jsonPath("$[0].description", is("Description1")))
               .andExpect(jsonPath("$[0].teacherId", is(1)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].name", is("Name2")))
               .andExpect(jsonPath("$[1].description", is("Description2")))
               .andExpect(jsonPath("$[1].teacherId", is(2)))
               .andDo(print());
    }

    @Test
    void getSubject() throws Exception {
        mockMvc.perform(get("/subjects/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.name", is("Name1")))
               .andExpect(jsonPath("$.description", is("Description1")))
               .andExpect(jsonPath("$.teacherId", is(1)))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addLesson() throws Exception {
        when(subjectService.addSubject(any(Subject.class))).thenAnswer(invocationOnMock -> {
            Subject subjectOnMock = invocationOnMock.getArgument(0);

            assertNull(subjectOnMock.getId());
            assertEquals("Name1", subjectOnMock.getName());
            assertEquals("Description1", subjectOnMock.getDescription());
            assertEquals(1L, subjectOnMock.getTeacher().getId());

            Subject newSubject = new Subject();
            newSubject.setName(subjectOnMock.getName());
            newSubject.setDescription(subjectOnMock.getDescription());
            newSubject.setTeacher(userService.findById(subjectOnMock.getTeacher().getId()));

            return newSubject;
        });

        mockMvc.perform(post("/subjects")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Name1\",\"description\":\"Description1\",\"teacherId\":1}")
        )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("Name1")))
               .andExpect(jsonPath("$.description", is("Description1")))
               .andExpect(jsonPath("$.teacherId", is(1)))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addLessonByTeacher_forbidden() throws Exception {
        mockMvc.perform(post("/subjects"))
               .andExpect(status().isForbidden())
               .andDo(print());
    }

    @Test
    void addLessonByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(post("/subjects"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSubject() throws Exception {
        mockMvc.perform(delete("/subjects/{id}", 2))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message", is("Przemiot o id 2 zostal usuniety")))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteSubjectByTeacher_forbidden() throws Exception {
        mockMvc.perform(delete("/subjects/{id}", 2))
               .andExpect(status().isForbidden())
               .andDo(print());
    }

    @Test
    void deleteSubjectByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(delete("/subjects/{id}", 2))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteStudentFromLesson() throws Exception {
        mockMvc.perform(delete("/subjects/{id}/deleteStudent/{studentId}", 1, 1))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message", is("Usunieto ucznia z przedmiotu")))
               .andDo(print());
    }

    @Test
    void deleteStudentFromLessonByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(delete("/subjects/{id}/deleteStudent/{studentId}", 1, 1))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }


    @Test
    @WithMockUser(roles = "TEACHER")
    void updateSubject() throws Exception {
        when(subjectService.updateSubject(any(SubjectDTO.class), any(Long.class))).thenAnswer(invocationOnMock -> {
            SubjectDTO subjectOnMock = invocationOnMock.getArgument(0);
            Long subjectIdOnMock = invocationOnMock.getArgument(1);

            assertEquals(1, subjectOnMock.getId());
            assertEquals("Name1", subjectOnMock.getName());
            assertEquals("Description1new", subjectOnMock.getDescription());

            assertEquals(1L, subjectIdOnMock);

            Subject newSubject = subjectService.findById(subjectIdOnMock);
            newSubject.setDescription(subjectOnMock.getDescription());

            return newSubject;
        });

        mockMvc.perform(put("/subjects/" + "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"Name1\",\"description\":\"Description1new\"}")
        )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("Name1")))
               .andExpect(jsonPath("$.description", is("Description1new")))
               .andDo(print());
    }

    @Test
    void updateSubjectByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(put("/subjects/" + "1"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }


}
