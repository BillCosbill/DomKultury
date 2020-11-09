package com.example.demo.services.implementations;

import com.example.demo.dto.RoomDTO;
import com.example.demo.exceptions.ConflictGlobalException;
import com.example.demo.exceptions.NotFoundGlobalException;
import com.example.demo.file.DBFile;
import com.example.demo.file.DBFileRepository;
import com.example.demo.mappers.RoomMapper;
import com.example.demo.models.Lesson;
import com.example.demo.models.Room;
import com.example.demo.repository.RoomRepository;
import com.example.demo.services.interfaces.LessonService;
import com.example.demo.services.interfaces.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final DBFileRepository imageRepository;
    private final LessonService lessonService;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper, DBFileRepository imageRepository, LessonService lessonService) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.imageRepository = imageRepository;
        this.lessonService = lessonService;
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono sali z id " + id));
    }

    @Override
    public List<Lesson> getRoomLessons(Long id) {
        Room room = findById(id);

        List<Lesson> lessons = room.getLessons();

        return lessons;
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = findById(id);

        if (lessonService.findAll().stream().filter(lesson -> lesson.getRoom() == room).findAny().isPresent()) {
            throw new ConflictGlobalException("W sali z id " + id + " są zaplanowana lekcje. Przed usunięciem sali należy usunąć lekcje odbywające się w tej sali!");
        }

        roomRepository.delete(room);
    }

    @Override
    public Room updateRoom(RoomDTO roomDTO, Long id, String imageId) {
        if (!roomDTO.getId().equals(id)) {
            throw new NotFoundGlobalException("Wystąpił błąd. Identyfikator sali nie został rozpoznany!");
        }

        Room roomOpt = findById(id);

        if (!roomOpt.getNumber().equals(roomDTO.getNumber()) && roomRepository.existsByNumber(roomDTO.getNumber())) {
            throw new ConflictGlobalException("Sala z numerem " + roomDTO.getNumber() + " już istnieje");
        }

        if (imageId != null) {
            Optional<DBFile> image = imageRepository.findById(imageId);

            if (image.isPresent()) {
                roomDTO.setImageId(imageId);
            }
        }

        Room room = roomMapper.toRoom(roomDTO, id);

        return save(room);
    }

    @Override
    public Room addRoom(Room room, String id) {
        if (roomRepository.existsByNumber(room.getNumber())) {
            throw new ConflictGlobalException("Sala z numerem " + room.getNumber() + " już istnieje");
        }

        if (id != null) {
            Optional<DBFile> image = imageRepository.findById(id);

            if (image.isPresent()) {
                room.setImage(id);
            }
        }

        return save(room);
    }

    @Override
    public void deleteImage(Long id) {
        Room room = findById(id);

        room.setImage(null);

        save(room);
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }
}
