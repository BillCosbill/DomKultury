package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    private String topic;

    @NotBlank
    private String description;

    @NotBlank
    private LocalDateTime startDate;

    @NotBlank
    private LocalDateTime finishDate;

    @NotBlank
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(	name = "lesson_room",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Room room;

    @OneToMany(mappedBy = "lesson")
    private List<Attendance> attendances;

    @NotBlank
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(	name = "lesson_subject",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
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
