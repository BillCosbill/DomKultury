package com.example.demo.models;

import com.example.demo.dto.StudentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmailMessage {

    private Long fromId;

    private String to;
    private String subject;
    private String text;

    private List<StudentDTO> studentList;
}
