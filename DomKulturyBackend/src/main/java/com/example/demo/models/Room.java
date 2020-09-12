package com.example.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String number;

    @NotBlank
    private String destiny;

    @NotBlank
    private String description;

    @NotBlank
    private int seats;

    @OneToMany(mappedBy = "room")
    private List<Event> events;

    public Room(String number, String destiny, String description, int seats) {
        this.number = number;
        this.destiny = destiny;
        this.description = description;
        this.seats = seats;
    }
}
