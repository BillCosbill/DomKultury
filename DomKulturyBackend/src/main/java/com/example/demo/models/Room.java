package com.example.demo.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numer nie moze byc pusty")
    private String number;

    @NotBlank(message = "Przeznaczenie nie moze byc puste")
    private String destiny;

    @NotBlank(message = "Opis nie moze byc pusty")
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
