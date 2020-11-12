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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String number;

    @NotNull
    private String destiny;

    @NotNull
    @Column(columnDefinition="LONGTEXT")
    private String description;

    private String image;

    @OneToMany(mappedBy = "room")
    private List<Lesson> lessons;

    public Room(String number, String destiny, String description) {
        this.number = number;
        this.destiny = destiny;
        this.description = description;
    }
}
