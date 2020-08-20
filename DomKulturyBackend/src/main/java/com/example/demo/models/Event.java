package com.example.demo.models;

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
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @ManyToOne(fetch = FetchType.EAGER) //TODO czemu przy FetchType.LAZY wywala błąd i program ?!?!?!?!
    @JoinTable(	name = "event_teacher",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private User teacher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "event_students",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<User> students;

    @NotBlank
    private int studentsLimit;


    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    public Event(String title, String description, User teacher, List<User> students, int studentsLimit, LocalDateTime startDate, LocalDateTime finishDate) {
        this.title = title;
        this.description = description;
        this.teacher = teacher;
        this.students = students;
        this.studentsLimit = studentsLimit;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }


}
