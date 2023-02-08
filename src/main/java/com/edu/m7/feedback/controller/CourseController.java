package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.model.repository.DateRepository;
import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.payload.response.QrCodeResponse;
import com.edu.m7.feedback.service.CourseService;
import com.edu.m7.feedback.service.LecturerService;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/courses")
@Slf4j
@CrossOrigin
public class CourseController {

    private final CourseService courseService;
    private final LecturerService lecturerService;

    @Autowired
    public CourseController(CourseService courseService, LecturerService lecturerService,
                            DateRepository dateRepository,
                            CourseRepository courseRepository) {
        this.courseService = courseService;
        this.lecturerService = lecturerService;
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

    @GetMapping("/next-three")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    ResponseEntity<List<CourseResponseDto>> getUpcomingCourses(Principal principal) {
        //get the current lecturer
        Lecturer lecturer = lecturerService.getLecturer(principal);

        //get the next three courses
        List<CourseResponseDto> coursesDto = courseService.getNextThreeCourses(lecturer);
        return new ResponseEntity<>(coursesDto, HttpStatus.OK);
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{courseId}")
    ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long courseId, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        Long courseLecturerId = courseService.getLecturerByCourseId(courseId);
        if (!courseLecturerId.equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return ResponseEntity.ok(courseService.getCourseById(courseId));
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
    @DeleteMapping("/{courseId}")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable Long courseId, Principal principal) {

        Long principalLecturer = lecturerService.getLecturer(principal).getLecturerId();

        try {
            Long courseLecturer = courseService.getLecturerByCourseId(courseId);
            if (!principalLecturer.equals(courseLecturer)) {
                return new ResponseEntity<>(
                        new MessageResponse("You are not authorized to delete this course"), HttpStatus.UNAUTHORIZED
                );
            }
            courseService.deleteCourse(courseId);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            log.info("Tried to delete course that was not found" + e);
            return new ResponseEntity<>(new MessageResponse("Course was not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new MessageResponse("Course deleted successfully"), HttpStatus.OK);
    }

    // Update a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestBody CourseRequestDto courseDto,
            Principal principal
    ) {

        // check if the given course exists
        if (null == courseService.getCourseById(courseId, lecturerService.getLecturer(principal))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if Lecturer is permitted to update
        if (!courseService.getLecturerByCourseId(courseId).equals(lecturerService.getLecturer(principal).getLecturerId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // update Course
        CourseResponseDto updatedCourse = courseService.updateCourse(courseId, courseDto);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);

    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{courseId}/all-evaluations")
    ResponseEntity<List<EvaluationResponseDto>> getEvaluation(@PathVariable Long courseId, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!courseService.getLecturerByCourseId(courseId).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(courseService.loadEvaluationsByCourseId(courseId));
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{courseId}/newest-evaluation")
    ResponseEntity<EvaluationResponseDto> getNewestEvaluation(@PathVariable Long courseId, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!courseService.getLecturerByCourseId(courseId).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(courseService.loadEvaluationsByCourseId(courseId).get(0));

    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{courseId}/qr-code")
    ResponseEntity<QrCodeResponse> getQRCodeForEvaluation(@PathVariable Long courseId, Principal principal) {
        Long lecturerId = lecturerService.getLecturer(principal).getLecturerId();
        if (!courseService.getLecturerByCourseId(courseId).equals(lecturerId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        EvaluationResponseDto evaluation = courseService.loadEvaluationsByCourseId(courseId).get(0);
        String shortcode = evaluation.getShortcode();
        byte[] image = new byte[0];
        try {
            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(shortcode, 250, 250);
        } catch (WriterException | IOException e) {
            log.error("Error creating QR code", e);
        }
        QrCodeResponse qrCodeResponse = new QrCodeResponse(image, shortcode);
        return new ResponseEntity<>(qrCodeResponse, HttpStatus.OK);
    }

    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/dates")
    ResponseEntity<List<String>> getAllDates(Principal principal) {
        Lecturer lecturer = lecturerService.getLecturer(principal);
        List<String> dates = courseService.getAllDates(lecturer);

        if (dates != null) {
            return new ResponseEntity<>(dates, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


}


