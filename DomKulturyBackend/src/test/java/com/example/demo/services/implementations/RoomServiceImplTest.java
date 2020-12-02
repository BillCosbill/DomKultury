package com.example.demo.services.implementations;

import com.example.demo.dto.RoomDTO;
import com.example.demo.exceptions.exception.ConflictGlobalException;
import com.example.demo.exceptions.exception.NotFoundGlobalException;
import com.example.demo.models.Lesson;
import com.example.demo.models.Room;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.RoomService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RoomServiceImplTest {

    @MockBean
    RoomRepository roomRepository;

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
    RoomService roomService;

    @BeforeEach
    void init() {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setNumber("01");
        room1.setDestiny("Test room 1");
        room1.setDescription("Test description 1");

        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setTopic("topic1");
        lesson1.setRoom(room1);
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setTopic("topic2");
        lesson2.setRoom(room1);

        room1.setLessons(Lists.newArrayList(lesson1, lesson2));

        Room room2 = new Room();
        room2.setId(2L);
        room2.setNumber("02");
        room2.setDestiny("Test room 2");
        room2.setDescription("Test description 2");

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room2));
        when(roomRepository.findAll()).thenReturn(Lists.newArrayList(room1, room2));

        when(lessonRepository.findAll()).thenReturn(Lists.newArrayList(lesson1, lesson2));
    }

    @Test
    void getRoom_success() {
        Room getRoom = roomService.findById(1L);

        assertEquals(1L, getRoom.getId());
        assertEquals("01", getRoom.getNumber());
        assertEquals("Test room 1", getRoom.getDestiny());
        assertEquals("Test description 1", getRoom.getDescription());
    }

    @Test
    void getRoom_whenRoomDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> roomService.findById(3L))
                .hasMessage("Nie znaleziono sali z id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void getRoomLesson() {
        List<Lesson> lesson1 = roomService.getRoomLessons(1L);
        List<Lesson> lesson2 = roomService.getRoomLessons(2L);

        assertEquals(2, lesson1.size());
        assertEquals("topic1", lesson1.get(0).getTopic());
        assertEquals("topic2", lesson1.get(1).getTopic());

        assertNull(lesson2);
    }

    @Test
    void getRoomLesson_whenRoomDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> roomService.getRoomLessons(3L))
                .hasMessage("Nie znaleziono sali z id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void deleteRoom_whenLessonsAreAssignedToRoom_thenThrowException() {
        assertThatThrownBy(() -> roomService.deleteRoom(1L))
                .hasMessage("W sali z id 1 są zaplanowana lekcje. Przed usunięciem sali należy usunąć lekcje odbywające się w tej sali!")
                .isInstanceOf(ConflictGlobalException.class);
    }

    @Test
    void deleteRoom_whenRoomDoesntExists_thenThrowException() {
        assertThatThrownBy(() -> roomService.deleteRoom(3L))
                .hasMessage("Nie znaleziono sali z id 3")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void updateRoom_whenIdConflictDetected_thenThrowException() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(3L);

        assertThatThrownBy(() -> roomService.updateRoom(roomDTO, 4L, null))
                .hasMessage("Wystapil blad. Id sali nie zostalo rozpoznane!")
                .isInstanceOf(NotFoundGlobalException.class);
    }

    @Test
    void addRoom_whenRoomNumberExists_thenThrowException() {
        Room room = new Room();
        room.setNumber("01");

        Mockito.when(roomRepository.existsByNumber(room.getNumber())).thenReturn(true);

        assertThatThrownBy(() -> roomService.addRoom(room, null))
                .hasMessage("Sala z numerem " + room.getNumber() + " już istnieje")
                .isInstanceOf(ConflictGlobalException.class);
    }
}
