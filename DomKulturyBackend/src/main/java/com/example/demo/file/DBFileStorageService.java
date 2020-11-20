package com.example.demo.file;

import com.example.demo.exceptions.ConflictGlobalException;
import com.example.demo.exceptions.NotFoundGlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepository dbFileRepository;

    public DBFile storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new ConflictGlobalException("Nazwa pliku zawiera błąd: " + fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), compressBytes(file.getBytes()));

            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new ConflictGlobalException("Nie udało się dodać pliku " + fileName + ". Spróbuj ponownie!");
        }
    }

    public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                               .orElseThrow(() -> new NotFoundGlobalException("Nie znaleziono pliku z id " + fileId));
    }

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
}
