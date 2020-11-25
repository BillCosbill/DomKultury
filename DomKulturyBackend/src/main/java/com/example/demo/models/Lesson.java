package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Temat nie moze byc pusty")
    private String topic;

    @NotBlank(message = "Opis nie moze byc pusty")
    @Column(columnDefinition="LONGTEXT")
    private String description;

    @NotNull(message = "Data rozpoczecia nie moze byc pusta")
    private LocalDateTime startDate;

    @NotNull(message = "Data zakonczenia nie moze byc pusta")
    private LocalDateTime finishDate;

    @NotNull(message = "Pokoj nie moze byc pusty")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
//    @JoinTable(	name = "lesson_room",
//            joinColumns = @JoinColumn(name = "lesson_id"),
//            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Room room;

    @OneToMany(mappedBy = "lesson")
    private List<Attendance> attendances;

    @NotNull(message = "Przedmiot nie moze byc pusty")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
//    @JoinTable(	name = "lesson_subject",
//            joinColumns = @JoinColumn(name = "lesson_id"),
//            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Subject subject;

    public Lesson(String topic, String description, LocalDateTime startDate, LocalDateTime finishDate, Room room, Subject subject) {
        this.topic = topic;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.room = room;
        this.subject = subject;
    }
}
