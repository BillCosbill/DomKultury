package com.example.demo.services.implementations;

import com.example.demo.dto.UserDTO;
import com.example.demo.exceptions.ConflictGlobalException;
import com.example.demo.exceptions.NotFoundGlobalException;
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    SubjectRepository subjectRepository;

    @MockBean
    LessonRepository lessonRepository;

    @MockBean
    AttendanceRepository attendanceRepository;

    @MockBean
    RoleRepository roleRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

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

        Mockito.when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(admin));
        Mockito.when(roleRepository.findByName(ERole.ROLE_TEACHER)).thenReturn(Optional.of(teacher));

        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        Mockito.when(userRepository.findAll()).thenReturn(Lists.newArrayList(user1, user2));
    }

    @Test
    void getUser_success() {
        User getUser = userService.findById(1L);

        assertEquals(1L, getUser.getId());
        assertEquals("testUsername1", getUser.getUsername());
        assertEquals("firstName1", getUser.getFirstName());
        assertEquals("lastName1", getUser.getLastName());
        assertEquals("test1@onet.pl", getUser.getEmail());
        assertTrue(getUser.isEnable());
    }

    @Test
    void getUser_whenUserDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> userService.findById(3L))
                .hasMessage("Nie znaleziono uzytkownika o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void deleteUser_whenUserDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> userService.deleteUser(3L))
                .hasMessage("Nie znaleziono uzytkownika o id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void updateUser_whenUserDoesntExists_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(3L);

        assertThatThrownBy(() -> userService.updateUser(userDTO, 4L))
                .hasMessage("Nie znaleziono uzytkownika o id 4")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void updateUser_whenIdConflictDetected_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        assertThatThrownBy(() -> userService.updateUser(userDTO, 2L))
                .hasMessage("Wystapil blad. Id uzytkownika nie zostalo rozpoznane!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void updateUser_whenEmailIsTakenByUser_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test2@onet.pl");

        Mockito.when(userRepository.existsByEmail("test2@onet.pl")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userDTO, 1L))
                .hasMessage("Uzytkownik z adresem email test2@onet.pl już istnieje!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void updateUser_whenEmailIsTakenByStudent_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test2@onet.pl");

        Mockito.when(studentRepository.existsByEmail("test2@onet.pl")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userDTO, 1L))
                .hasMessage("Uczen z adresem email test2@onet.pl już istnieje!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void updateUser_whenUsernameIsTaken_thenThrowException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUsername2");

        Mockito.when(userRepository.existsByUsername("testUsername2")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userDTO, 1L))
                .hasMessage("Uzytkownik z nazwa testUsername2 już istnieje!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void changeRole() {
        Role roleTeacher = roleRepository.findByName(ERole.ROLE_TEACHER).get();
        User user = userRepository.findById(1L).get();

        Set<Role> teacherRoles = new HashSet<>();
        teacherRoles.add(roleTeacher);
        userService.changeRole(ERole.ROLE_TEACHER, user.getId());

        assertEquals(teacherRoles, user.getRoles());
    }
}
