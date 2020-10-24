package com.example.demo.mappers;

import com.example.demo.dto.RoomDTO;
import com.example.demo.exceptions.EventNotFoundException;
import com.example.demo.file.DBFileRepository;
import com.example.demo.models.Lesson;
import com.example.demo.models.Room;
import com.example.demo.repository.LessonRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomMapper {

    RoomRepository roomRepository;
    DBFileRepository imageRepository;
    LessonRepository lessonRepository;

    @Autowired
    public RoomMapper(RoomRepository roomRepository, DBFileRepository imageRepository, LessonRepository lessonRepository) {
        this.roomRepository = roomRepository;
        this.imageRepository = imageRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<RoomDTO> toRoomsDTO(List<Room> rooms) {
        return ((List<RoomDTO>) rooms.stream()
                                     .map(this::toRoomDTO)
                                     .collect(Collectors.toList()));
    }

    public RoomDTO toRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setNumber(room.getNumber());
        roomDTO.setDestiny(room.getDestiny());
        roomDTO.setDescription(room.getDescription());
        if (room.getImage() != null) {
            roomDTO.setImageId(room.getImage());
        }
        List<Long> lessonsId = new ArrayList<>();
        room.getLessons()
            .forEach(x -> lessonsId.add(x.getId()));
        roomDTO.setLessonsId(lessonsId);

        return roomDTO;
    }

    public Room toRoom(RoomDTO roomDTO, Long id) {
        Room room = roomRepository.findById(id)
                                  .orElseThrow(() -> new EventNotFoundException(id));

        if (roomDTO.getId() != null) {
            room.setId(roomDTO.getId());
        }
        if (roomDTO.getNumber() != null) {
            room.setNumber(room.getNumber());
        }
        if (roomDTO.getDestiny() != null) {
            room.setDestiny(roomDTO.getDestiny());
        }
        if (roomDTO.getDescription() != null) {
            room.setDescription(roomDTO.getDescription());
        }
        if (roomDTO.getImageId() != null) {
            room.setImage(roomDTO.getImageId());
        }
        if (roomDTO.getLessonsId() != null) {
            List<Lesson> lessons = new ArrayList<>();
            roomDTO.getLessonsId()
                   .forEach(x -> lessons.add(lessonRepository.findById(x)
                                                           .orElseThrow(() -> new EventNotFoundException(x))));
            room.setLessons(lessons);
        }

        return room;
    }
}
