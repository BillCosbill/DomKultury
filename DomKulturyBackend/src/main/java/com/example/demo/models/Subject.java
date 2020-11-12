package com.example.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(columnDefinition="LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(	name = "subject_teacher",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private User teacher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "subject_students",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    @OneToMany(mappedBy = "subject")
    private List<Lesson> lessons;

    public Subject(String name, String description, User teacher, List<Student> students) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.students = students;
    }

    public Subject(String name, String description, User teacher, List<Student> students, List<Lesson> lessons) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.students = students;
        this.lessons = lessons;
    }

}
