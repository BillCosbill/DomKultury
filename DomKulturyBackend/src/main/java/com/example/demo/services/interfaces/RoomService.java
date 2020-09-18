package com.example.demo.services.interfaces;

import com.example.demo.dto.RoomDTO;
import com.example.demo.models.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    List<Room> findAll();

    Room findById(Long id);

    void deleteRoom(Long id);

    Room updateRoom(RoomDTO roomDTO, Long id);

    Room save(Room room);

    Room addRoom(Room room, String id);
}
