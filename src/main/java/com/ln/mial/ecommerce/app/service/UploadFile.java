package com.ln.mial.ecommerce.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadFile {

    private final String FOLDER = "/app/images/";
    private final String IMG_DEFAULT = "default.png";

    public String upload(MultipartFile multipartFile) throws IOException {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return IMG_DEFAULT;
        }

        Path folderPath = Paths.get(FOLDER);

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        Path filePath = folderPath.resolve(fileName);

        Files.write(filePath, multipartFile.getBytes());

        return fileName;
    }

    public void delete(String nameFile) {
        Path path = Paths.get(FOLDER + nameFile);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("Error al eliminar imagen: " + e.getMessage());
        }
    }
}