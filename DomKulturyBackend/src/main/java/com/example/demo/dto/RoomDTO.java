package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    @NotBlank(message = "Numer nie moze byc pusty")
    private String number;
    @NotBlank(message = "Przeznaczenie nie moze byc puste")
    private String destiny;
    @NotBlank(message = "Opis nie moze byc pusty")
    private String description;
    private String imageId;
    private List<Long> lessonsId;
}
