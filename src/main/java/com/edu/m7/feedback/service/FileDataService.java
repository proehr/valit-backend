package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.entity.FileData;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileDataService {
    private String folderPath;

    private final FileDataRepository fileDataRepository;

    public FileDataService(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }

    @Value("${valit.app.files.directory}")
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String uploadFile(MultipartFile file, Lecturer lecturer) throws IOException {
        File lecturerFolder = new File(folderPath, String.valueOf(lecturer.getLecturerId()));
        if (!lecturerFolder.exists()) {
            lecturerFolder.mkdirs();
        }
        Path filePath = Path.of(lecturerFolder.toString(), file.getOriginalFilename());
        file.transferTo(filePath);

        fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath.toString())
                .lecturer(lecturer).build());

        return "file uploaded successfully : " + file.getOriginalFilename();
    }

    public byte[] downloadFile(String filePath) throws IOException {
        return Files.readAllBytes(new File(filePath).toPath());
    }

    public String getFilePath(Lecturer lecturer) {
        return fileDataRepository.findByLecturer(lecturer).orElseThrow().getFilePath();
    }


}
