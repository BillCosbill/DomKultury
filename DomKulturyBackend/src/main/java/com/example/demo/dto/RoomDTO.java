package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private String number;
    private String destiny;
    private String description;
    private int seats;
    private Long imageId;
    private List<Long> eventsId;
}
