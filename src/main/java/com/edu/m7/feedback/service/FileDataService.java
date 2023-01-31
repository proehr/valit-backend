package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.entity.FileData;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;
    private static final String FOLDER_NAME = "feedback-backend/repo/";
    private final String FOLDER_PATH = System.getProperty("user.home") + "/IdeaProjects/" + FOLDER_NAME;

    public String uploadFile(MultipartFile file, Lecturer lecturer) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        FileData fileData=fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .lecturer(lecturer).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadFile(String filePath) throws IOException {
        byte[] file = Files.readAllBytes(new File(filePath).toPath());
        return file;
    }


    public String getFilePath(Lecturer lecturer){
        return fileDataRepository.findByLecturer(lecturer).get().getFilePath();
    }


}
