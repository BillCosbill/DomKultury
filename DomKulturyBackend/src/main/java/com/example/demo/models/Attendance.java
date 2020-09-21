package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(	name = "attendance_lesson",
            joinColumns = @JoinColumn(name = "attendance_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private Lesson lesson;

    @NotBlank
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(	name = "attendance_student",
            joinColumns = @JoinColumn(name = "attendance_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Student student;

    @NotBlank
    private boolean present;

    public Attendance(Lesson lesson, Student student, boolean present) {
        this.lesson = lesson;
        this.student = student;
        this.present = present;
    }
}
