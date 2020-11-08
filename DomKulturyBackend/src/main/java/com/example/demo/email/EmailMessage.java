package com.example.demo.email;

import com.example.demo.dto.StudentDTO;
import com.example.demo.models.Student;
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

    public EmailMessage(String to, String subject, String text, List<StudentDTO> studentList) {
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.studentList = studentList;
    }
}
