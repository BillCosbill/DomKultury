package com.example.demo.controllers;

import com.example.demo.models.File;
import com.example.demo.models.Image;
import com.example.demo.payload.response.UploadFileResponse;
import com.example.demo.services.implementations.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@CrossOrigin(origins = "*")

@RestController
public class FileController {

    private final FileServiceImpl fileServiceImpl;

    @Autowired
    public FileController(FileServiceImpl fileServiceImpl) {
        this.fileServiceImpl = fileServiceImpl;
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        File dbFile = fileServiceImpl.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                            .path("/downloadFile/")
                                                            .path(dbFile.getId())
                                                            .toUriString();

        return new UploadFileResponse(dbFile.getId(), dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping("/downloadFile/{fileId}")
    public Image downloadFile(@PathVariable String fileId) {
        File file = fileServiceImpl.getFile(fileId);

        return new Image(file.getFileName(), file.getFileType(), decompressBytes(file.getData()));
    }

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
        } catch (IOException | DataFormatException ioe) {
            ioe.printStackTrace();
        }
        return outputStream.toByteArray();
    }

}
