package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.FileData;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.FileDataRepository;
import com.edu.m7.feedback.payload.response.ImageResponse;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.service.FileDataService;
import com.edu.m7.feedback.service.LecturerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.NoSuchElementException;
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
    public ResponseEntity<ImageResponse> getProfilePicture(Principal principal) throws IOException {

        Lecturer lecturer = lecturerService.getLecturer(principal);
        try {
            String filePath = fileDataService.getFilePath(lecturer);
            byte[] imageData = fileDataService.downloadFile(filePath);
            return ResponseEntity.status(HttpStatus.OK).body(new ImageResponse(imageData));
        } catch (NoSuchElementException e) {
            log.info("Requested non-existent picture", e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
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
