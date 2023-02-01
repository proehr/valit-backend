package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.service.CourseService;
import com.edu.m7.feedback.service.LecturerService;
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
    public CourseController(CourseService courseService, LecturerService lecturerService) {
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


    // retrieve a single course by id
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable Long id, Principal principal) {
        Lecturer lecturer = lecturerService.getLecturer(principal);
        CourseResponseDto courseDto = courseService.getCourseById(id, lecturer);

        if (courseDto != null) {
            return new ResponseEntity<>(courseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
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

}


