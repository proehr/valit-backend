package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.service.CourseService;
import com.edu.m7.feedback.service.EvaluationService;
import com.edu.m7.feedback.service.LecturerService;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/courses")
@Slf4j
@CrossOrigin
public class CourseController {

    private final CourseService courseService;
    private final LecturerService lecturerService;
    private final EvaluationService evaluationService;

    @Autowired
    public CourseController(CourseService courseService, LecturerService lecturerService, EvaluationService evaluationService) {
        this.courseService = courseService;
        this.lecturerService = lecturerService;
        this.evaluationService = evaluationService;
    }

    // retrieve all the courses of the currently logged in Lecturer
    @GetMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    public ResponseEntity<List<CourseResponseDto>> getAllCourses(Principal principal) {

        Lecturer lecturer = lecturerService.getLecturer(principal);
        List<CourseResponseDto> coursesDto = courseService.getAllCourses(lecturer);

        if (coursesDto != null) {
            return new ResponseEntity<>(coursesDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
    // Create a new Course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(
            @RequestBody CourseRequestDto courseDto,
            Principal principal
    ) {

        Lecturer lecturer = lecturerService.getLecturer(principal);
        CourseResponseDto savedCourse = courseService.createCourse(courseDto, lecturer);

        if (savedCourse != null) {
            return new ResponseEntity<>(savedCourse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // Delete a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable Long id, Principal principal) {

        Long principalLecturer = lecturerService.getLecturer(principal).getLecturerId();

        try {
            Long courseLecturer = courseService.getLecturerByCourseId(id);
            if (!principalLecturer.equals(courseLecturer)) {
                return new ResponseEntity<>(
                        new MessageResponse("You are not authorized to delete this course"), HttpStatus.UNAUTHORIZED
                );
            }
            courseService.deleteCourse(id);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            log.info("Tried to delete course that was not found" + e);
            return new ResponseEntity<>(new MessageResponse("Course was not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new MessageResponse("Course deleted successfully"), HttpStatus.OK);
    }

    // Update a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable("id") Long id,
            @RequestBody CourseRequestDto courseDto,
            Principal principal
    ) {

        // check if the given course exists
        if (null == courseService.getCourseById(id, lecturerService.getLecturer(principal))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if Lecturer is permitted to update
        if (!courseService.getLecturerByCourseId(id).equals(lecturerService.getLecturer(principal).getLecturerId())) {
           return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        // update Course
        CourseResponseDto updatedCourse = courseService.updateCourse(id, courseDto);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);

    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}/all-evaluations")
    ResponseEntity<List<EvaluationResponseDto>> getEvaluation(@PathVariable Long id, Principal principal) {
       Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
       if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return ResponseEntity.ok(courseService.loadEvaluationByCourseId(id));
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}/newest-evaluation")
    ResponseEntity<EvaluationResponseDto> getNewestEvaluation(@PathVariable Long id, Principal principal) {
         Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
         if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
           return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return ResponseEntity.ok(courseService.loadEvaluationByCourseId(id).get(0));

    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}/qr-code")
    ResponseEntity<byte[]> getQRCodeForEvaluation(@PathVariable Long id, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!evaluationService.getLecturerIdByEvaluationId(id).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        EvaluationResponseDto evaluation = courseService.loadEvaluationByCourseId(id).get(0);

        String code = evaluation.getShortcode().toString();


        byte[] image = new byte[0];
        try {
            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(code,250,250);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }


        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"image.png")
                .header("Shortcode",  evaluation.getShortcode().toString() )
                .contentType(MediaType.IMAGE_PNG)
                .body(image);

    }

}


