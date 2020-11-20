package com.example.demo.email;

import com.example.demo.dto.StudentDTO;
import com.example.demo.exceptions.NotFoundGlobalException;
import com.example.demo.mappers.StudentMapper;
import com.example.demo.models.Student;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender javaMailSender, StudentMapper studentMapper, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.studentMapper = studentMapper;
        this.userRepository = userRepository;
    }

    public void registerMailMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        text = text.replaceAll("(\r\n|\n)", "<br />");

        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void remindPassword(String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        text = text.replaceAll("(\r\n|\n)", "<br />");

        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(Long fromId, String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        Optional<User> userOpt = userRepository.findById(fromId);

        if (!userOpt.isPresent()) {
            throw new NotFoundGlobalException("Nie znaleziono użytkownika o id " + fromId);
        }

        User user = userOpt.get();

        text = text.replaceAll("(\r\n|\n)", "<br />");

        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);

            String signature = "<br><br>Wysłane przez: <br>" + user.getFirstName() + " " + user
                    .getLastName() + "<br>" + user.getEmail();


            mimeMessageHelper.setText(text + signature, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendMultipleMails(Long fromId, String subject, String text, List<StudentDTO> studentList) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        Optional<User> userOpt = userRepository.findById(fromId);

        if (!userOpt.isPresent()) {
            throw new NotFoundGlobalException("Nie znaleziono użytkownika o id " + fromId);
        }

        User user = userOpt.get();

        text = text.replaceAll("(\r\n|\n)", "<br />");

        List<Student> students = new ArrayList<>();

        studentList.forEach(studentDTO -> students.add(studentMapper.toStudent(studentDTO, studentDTO.getId())));

        String finalText = text;
        students.forEach(student -> {
            MimeMessageHelper mimeMessageHelper = null;
            try {
                mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setTo(student.getEmail());
                mimeMessageHelper.setSubject(subject);

                String signature = "<br><br>Wysłane przez: <br>" + user.getFirstName() + " " + user
                        .getLastName() + "<br>" + user.getEmail();

                mimeMessageHelper.setText(finalText + signature, true);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

    }
}
