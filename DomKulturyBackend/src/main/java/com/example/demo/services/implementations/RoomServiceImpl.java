package com.example.demo.services.implementations;

import com.example.demo.dto.RoomDTO;
import com.example.demo.exceptions.RoomExistsException;
import com.example.demo.exceptions.RoomIsUsedInEventException;
import com.example.demo.exceptions.RoomNotFoundException;
import com.example.demo.mappers.RoomMapper;
import com.example.demo.models.Event;
import com.example.demo.models.Image;
import com.example.demo.models.Room;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;
    EventRepository eventRepository;
    RoomMapper roomMapper;
    ImageRepository imageRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper, EventRepository eventRepository, ImageRepository imageRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.eventRepository = eventRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = findById(id);
        List<Event> events = eventRepository.findAll();

        events.forEach(x -> {
            if(x.getRoom() == room) {
                throw new RoomIsUsedInEventException(id);
            }
        });

        roomRepository.delete(room);
    }

    @Override
    public Room updateRoom(RoomDTO roomDTO, Long id) {
        //TODO inny wyjątek, żeby się zgadzało
        if (!roomDTO.getId().equals(id)) {
            throw new RoomNotFoundException(id);
        }

        Room room = roomMapper.toRoom(roomDTO, id);

        return save(room);
    }

    @Override
    public Room addRoom(Room room, Long id) {
        if(roomRepository.existsByNumber(room.getNumber())){
            throw new RoomExistsException(room.getNumber());
        }

        Optional<Image> image = imageRepository.findById(id);

        if(image.isPresent()){
            room.setImage(image.get());
        }

        return save(room);
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }
}
