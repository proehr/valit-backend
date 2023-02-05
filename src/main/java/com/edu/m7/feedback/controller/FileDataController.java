package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.FileData;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.FileDataRepository;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.service.FileDataService;
import com.edu.m7.feedback.service.LecturerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/images/")
@Slf4j
@CrossOrigin
public class FileDataController {

    private final FileDataService fileDataService;

    private final LecturerService lecturerService;
    private final FileDataRepository fileDataRepository;

    public FileDataController(
            FileDataService fileDataService,
            LecturerService lecturerService,
            FileDataRepository fileDataRepository
    ) {
        this.fileDataService = fileDataService;
        this.lecturerService = lecturerService;
        this.fileDataRepository = fileDataRepository;
    }

    //Upload or Update the User's profile Picture
    @PostMapping
    public ResponseEntity<String> uploadOrUpdatePicture(
            @RequestParam("image") MultipartFile file,
            Principal principal
    ) throws Exception {

        Lecturer lecturer = lecturerService.getLecturer(principal);
        String uploadImage;

        Optional<FileData> fileData = fileDataRepository.findByLecturer(lecturer);
        if (fileData.isPresent()) {
            try {
                Files.delete(Path.of(fileData.get().getFilePath()));
                fileDataRepository.delete(fileData.get());
            } catch (Exception e) {
                log.info("Could not delete profile picture: " + e);
            }
        }

        uploadImage = fileDataService.uploadFile(file, lecturer);

        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    // Retrieve User's Profile Picture
    @GetMapping
    public ResponseEntity<byte[]> getProfilePicture(Principal principal) throws IOException {

        Lecturer lecturer = lecturerService.getLecturer(principal);

        String filePath = fileDataService.getFilePath(lecturer);

        byte[] imageData = fileDataService.downloadFile(filePath);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    // Delete the User's profile picture
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteImage(Principal principal) {
        Lecturer lecturer = lecturerService.getLecturer(principal);
        Optional<FileData> optionalFileData = fileDataRepository.findByLecturer(lecturer);

        if (optionalFileData.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("Profile Image not found!"), HttpStatus.NOT_FOUND);
        }
        fileDataRepository.delete(optionalFileData.get());
        try {
            Files.delete(Path.of(optionalFileData.get().getFilePath()));
        } catch (Exception e) {
            log.info("Could not delete profile picture: " + e);
        }

        return new ResponseEntity<>(new MessageResponse("Profile image deleted successfully"), HttpStatus.OK);
    }

}
