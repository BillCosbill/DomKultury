package com.example.demo.controllers;

import com.example.demo.dto.LessonDTO;
import com.example.demo.dto.RoomDTO;
import com.example.demo.mappers.LessonMapper;
import com.example.demo.mappers.RoomMapper;
import com.example.demo.models.Room;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;
    private final LessonMapper lessonMapper;

    @Autowired
    public RoomController(RoomService roomService, RoomMapper roomMapper, LessonMapper lessonMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getRooms() {
        return ResponseEntity.ok(roomMapper.toRoomsDTO(roomService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomMapper.toRoomDTO(roomService.findById(id)));
    }

    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<LessonDTO>> getRoomLessons(@PathVariable Long id) {
        return ResponseEntity.ok(lessonMapper.toLessonsDTO(roomService.getRoomLessons(id)));
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room, @RequestParam String imageId) {
        return roomService.addRoom(room, imageId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);

        return ResponseEntity.ok(new MessageResponse("Room deleted successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@RequestBody RoomDTO roomDTO, @PathVariable Long id, @RequestParam String imageId) {
        roomService.updateRoom(roomDTO, id, imageId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(roomDTO);
    }

    @PatchMapping("/{id}/deleteImage")
    public ResponseEntity<MessageResponse> deleteImageFromRoom(@PathVariable Long id) {
        roomService.deleteImage(id);

        return ResponseEntity.ok(new MessageResponse("Image removed successfully from lesson"));
    }
}
