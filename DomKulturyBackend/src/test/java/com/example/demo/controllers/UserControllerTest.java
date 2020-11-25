package com.example.demo.controllers;

import com.example.demo.dto.StudentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.Student;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.StudentService;
import com.example.demo.services.interfaces.UserService;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
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
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @BeforeEach
    void init() {
        Role teacher = new Role(ERole.ROLE_TEACHER);
        Role admin = new Role(ERole.ROLE_ADMIN);

        Set<Role> teacherRole = new HashSet<>();
        Set<Role> adminRole = new HashSet<>();
        teacherRole.add(teacher);
        adminRole.add(admin);

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testUsername1");
        user1.setFirstName("firstName1");
        user1.setLastName("lastName1");
        user1.setEmail("test1@onet.pl");
        user1.setPassword("$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2");
        user1.setRoles(adminRole);
        user1.setEnable(true);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testUsername2");
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEmail("test2@onet.pl");
        user2.setPassword("$2a$10$3ZP90w4a0j7aDReadREEQutjB69O9RPeufNNxZaIszvll.aDlSeI2");
        user2.setRoles(teacherRole);
        user2.setEnable(false);

        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);
        when(userService.findAll()).thenReturn(Lists.newArrayList(user1, user2));
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].username", is("testUsername1")))
               .andExpect(jsonPath("$[0].firstName", is("firstName1")))
               .andExpect(jsonPath("$[0].lastName", is("lastName1")))
               .andExpect(jsonPath("$[0].email", is("test1@onet.pl")))
               .andExpect(jsonPath("$[0].enable", is(true)))
               .andExpect(jsonPath("$[1].id", is(2)))
               .andExpect(jsonPath("$[1].username", is("testUsername2")))
               .andExpect(jsonPath("$[1].firstName", is("firstName2")))
               .andExpect(jsonPath("$[1].lastName", is("lastName2")))
               .andExpect(jsonPath("$[1].email", is("test2@onet.pl")))
               .andExpect(jsonPath("$[1].enable", is(false)))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getUserSubjects() throws Exception {
        mockMvc.perform(get("/users/1/subjects"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void getUserSubjectsByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(get("/users/1/subjects"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.username", is("testUsername1")))
               .andExpect(jsonPath("$.firstName", is("firstName1")))
               .andExpect(jsonPath("$.lastName", is("lastName1")))
               .andExpect(jsonPath("$.email", is("test1@onet.pl")))
               .andExpect(jsonPath("$.enable", is(true)))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 2))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message", is("Uzytkownik o identyfikatorze 2 zostal usuniety")))
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteUserByTeacher_forbidden() throws Exception {
        mockMvc.perform(delete("/users/{id}", 2))
               .andExpect(status().isForbidden())
               .andDo(print());
    }

    @Test
    void deleteUserByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(delete("/users/{id}", 2))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void updateUser() throws Exception {
        when(userService.updateUser(any(UserDTO.class), any(Long.class))).thenAnswer(invocationOnMock -> {
            UserDTO userOnMock = invocationOnMock.getArgument(0);
            Long studentIdOnMock = invocationOnMock.getArgument(1);

            assertEquals(1L, userOnMock.getId());
            assertEquals("testUsername1new", userOnMock.getUsername());
            assertEquals("firstName1new", userOnMock.getFirstName());
            assertEquals("lastName1new", userOnMock.getLastName());
            assertEquals("test1new@onet.pl", userOnMock.getEmail());
            assertTrue(userOnMock.isEnable());

            assertEquals(1L, studentIdOnMock);

            User newUser = new User();
            newUser.setId(userOnMock.getId());
            newUser.setUsername(userOnMock.getUsername());
            newUser.setFirstName(userOnMock.getFirstName());
            newUser.setLastName(userOnMock.getLastName());
            newUser.setEmail(userOnMock.getEmail());
            newUser.setEnable(userOnMock.isEnable());

            return newUser;
        });

        mockMvc.perform(put("/users/" + "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"username\":\"testUsername1new\",\"firstName\":\"firstName1new\",\"lastName\":\"lastName1new\",\"email\":\"test1new@onet.pl\",\"enable\":true}")
        )
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.username", is("testUsername1new")))
               .andExpect(jsonPath("$.firstName", is("firstName1new")))
               .andExpect(jsonPath("$.lastName", is("lastName1new")))
               .andExpect(jsonPath("$.email", is("test1new@onet.pl")))
               .andExpect(jsonPath("$.enable", is(true)))
               .andDo(print());
    }

    @Test
    void updateUserByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(put("/users/" + "1"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeRole() throws Exception {
        mockMvc.perform(patch("/users/changeRole/" + "1")
                .param("newRole", "ROLE_ADMIN"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void changeRoleByTeacher_forbidden() throws Exception {
        mockMvc.perform(patch("/users/changeRole/" + "1")
                .param("newRole", "ROLE_ADMIN"))
               .andExpect(status().isForbidden())
               .andDo(print());
    }

    @Test
    void changeRoleByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(patch("/users/changeRole/" + "1")
                .param("newRole", "ROLE_ADMIN"))
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void changePassword() throws Exception {
        mockMvc.perform(patch("/users/changePassword/" + "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"pass\",\"mewPassword\":\"newpass\"}")
        )
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    void changePasswordByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(patch("/users/changePassword/" + "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\":\"pass\",\"mewPassword\":\"newpass\"}")
        )
               .andExpect(status().isUnauthorized())
               .andDo(print());
    }

    @Test
    void generateNewPasswordByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(patch("/users/generateNewPassword/" + "test@onet.pl"))
               .andExpect(status().isOk())
               .andDo(print());
    }
}

