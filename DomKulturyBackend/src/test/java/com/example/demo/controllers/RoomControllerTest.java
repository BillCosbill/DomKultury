package com.example.demo.controllers;

import com.example.demo.dto.RoomDTO;
import com.example.demo.mappers.RoomMapper;
import com.example.demo.models.Lesson;
import com.example.demo.models.Room;
import com.example.demo.services.interfaces.RoomService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class RoomControllerTest {

    @MockBean
    RoomService roomService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RoomMapper roomMapper;

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
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setTopic("topic2");

        room1.setLessons(Lists.newArrayList(lesson1, lesson2));

        Room room2 = new Room();
        room2.setId(2L);
        room2.setNumber("02");
        room2.setDestiny("Test room 2");
        room2.setDescription("Test description 2");

        when(roomService.findById(1L)).thenReturn(room1);
        when(roomService.findById(2L)).thenReturn(room2);
        when(roomService.findAll()).thenReturn(Lists.newArrayList(room1, room2));
    }

    @Test
    void getRooms() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].number", is("01")))
                .andExpect(jsonPath("$[0].destiny", is("Test room 1")))
                .andExpect(jsonPath("$[0].description", is("Test description 1")))
                .andExpect(jsonPath("$[0].lessonsId", is(Lists.newArrayList(1, 2))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].number", is("02")))
                .andExpect(jsonPath("$[1].destiny", is("Test room 2")))
                .andExpect(jsonPath("$[1].description", is("Test description 2")))
                .andDo(print());
    }


    @Test
    void getRoom() throws Exception {
        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.number", is("01")))
                .andExpect(jsonPath("$.destiny", is("Test room 1")))
                .andExpect(jsonPath("$.description", is("Test description 1")))
                .andExpect(jsonPath("$.lessonsId", is(Lists.newArrayList(1, 2))))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addRoom() throws Exception {
        when(roomService.addRoom(any(Room.class), any(String.class))).thenAnswer(invocationOnMock -> {
            Room roomOnMock = invocationOnMock.getArgument(0);
            String imageIdOnMock = invocationOnMock.getArgument(1);

            assertNull(roomOnMock.getId());
            assertEquals("01", roomOnMock.getNumber());
            assertEquals("Test room 1", roomOnMock.getDestiny());
            assertEquals("Test description 1", roomOnMock.getDescription());

            assertEquals("1", imageIdOnMock);

            Room newRoom = new Room();
            newRoom.setNumber(roomOnMock.getNumber());
            newRoom.setDestiny(roomOnMock.getDestiny());
            newRoom.setDescription(roomOnMock.getDescription());

            return newRoom;
        });

        mockMvc.perform(post("/rooms").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"01\",\"destiny\":\"Test room 1\",\"description\":\"Test description 1\"}")
                .param("imageId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is("01")))
                .andExpect(jsonPath("$.destiny", is("Test room 1")))
                .andExpect(jsonPath("$.description", is("Test description 1")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addRoomWithInvalidArguments() throws Exception {
        mockMvc.perform(post("/rooms").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":null,\"destiny\":\"Test room 1\",\"description\":\"Test description 1\"}")
                .param("imageId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'message':'Numer nie moze byc pusty. '}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addRoomByTeacher_forbidden() throws Exception {
        mockMvc.perform(post("/rooms").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":01,\"destiny\":\"Test room 1\",\"description\":\"Test description 1\"}")
                .param("imageId", "1"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void addRoomByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(post("/rooms").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":01,\"destiny\":\"Test room 1\",\"description\":\"Test description 1\"}")
                .param("imageId", "1"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addRoomWithInvalidDescription() throws Exception {
        mockMvc.perform(post("/rooms").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"01\",\"destiny\":\"Test room 1\",\"description\":\"\"}")
                .param("imageId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'message':'Opis nie moze byc pusty. '}"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRoom() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Pokoj o id 2 zostal usuniety pomyslnie.")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteRoomByTeacher_forbidden() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 2))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void deleteRoomByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 2))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRoom() throws Exception {
        when(roomService.updateRoom(any(RoomDTO.class), any(Long.class), any(String.class))).thenAnswer(invocationOnMock -> {
            RoomDTO roomOnMock = invocationOnMock.getArgument(0);
            Long roomIdOnMock = invocationOnMock.getArgument(1);
            String imageIdOnMock = invocationOnMock.getArgument(2);

            assertEquals(1L, roomOnMock.getId());
            assertEquals("011", roomOnMock.getNumber());
            assertEquals("Test room 11", roomOnMock.getDestiny());
            assertEquals("Test description 11", roomOnMock.getDescription());

            assertEquals(1L, roomIdOnMock);

            assertEquals("1", imageIdOnMock);

            Room newRoom = new Room();
            newRoom.setId(roomOnMock.getId());
            newRoom.setNumber(roomOnMock.getNumber());
            newRoom.setDestiny(roomOnMock.getDestiny());
            newRoom.setDescription(roomOnMock.getDescription());

            return newRoom;
        });

        mockMvc.perform(put("/rooms/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"number\":\"011\",\"destiny\":\"Test room 11\",\"description\":\"Test description 11\"}")
                .param("imageId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.number", is("011")))
                .andExpect(jsonPath("$.destiny", is("Test room 11")))
                .andExpect(jsonPath("$.description", is("Test description 11")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void updateRoomByTeacher_forbidden() throws Exception {
        mockMvc.perform(put("/rooms/" + "1"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void updateRoomByNotLoggedIn_unauthorized() throws Exception {
        mockMvc.perform(put("/rooms/" + "1"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
