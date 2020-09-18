package com.example.demo.mappers;

import com.example.demo.dto.RoomDTO;
import com.example.demo.exceptions.EventNotFoundException;
import com.example.demo.exceptions.ImageNotFoundException;
import com.example.demo.file.DBFileRepository;
import com.example.demo.models.Event;
import com.example.demo.models.Room;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomMapper {

    RoomRepository roomRepository;
    EventRepository eventRepository;
    DBFileRepository imageRepository;

    @Autowired
    public RoomMapper(RoomRepository roomRepository, EventRepository eventRepository, DBFileRepository imageRepository) {
        this.roomRepository = roomRepository;
        this.eventRepository = eventRepository;
        this.imageRepository = imageRepository;
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
        roomDTO.setSeats(room.getSeats());

        List<Long> eventsIds = new ArrayList<>();
        room.getEvents()
            .forEach(x -> eventsIds.add(x.getId()));
        roomDTO.setEventsId(eventsIds);

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
        if (roomDTO.getSeats() > 0) {
            room.setSeats(roomDTO.getSeats());
        }

        if (roomDTO.getEventsId() != null) {
            List<Event> events = new ArrayList<>();
            roomDTO.getEventsId()
                   .forEach(x -> events.add(eventRepository.findById(x)
                                                           .orElseThrow(() -> new EventNotFoundException(x))));
            room.setEvents(events);
        }

        return room;
    }
}
