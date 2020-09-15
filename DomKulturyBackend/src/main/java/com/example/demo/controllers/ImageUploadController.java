package com.example.demo.controllers;

import com.example.demo.dto.EventDTO;
import com.example.demo.models.Image;
import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/image")
public class ImageUploadController {
    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/upload")
    public ResponseEntity<Long> uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
        Image img = new Image(file.getOriginalFilename(), file.getContentType(), compressBytes(file.getBytes()));
        imageRepository.save(img);
        return ResponseEntity.ok(img.getId());
    }

    @GetMapping(path = {"/get/{imageId}"})
    public Image getImage(@PathVariable("imageId") Long id) throws IOException {
        final Optional<Image> retrievedImage = imageRepository.findById(id);
        Image img = new Image(retrievedImage.get()
                                            .getName(), retrievedImage.get()
                                                                      .getType(),
                decompressBytes(retrievedImage.get()
                                              .getPicByte()));
        return img;
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
