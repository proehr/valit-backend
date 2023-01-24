package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.dto.CourseDto;
import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.AccountRepository;
import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import com.edu.m7.feedback.payload.response.MessageResponse;
import com.edu.m7.feedback.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;
    private final AccountRepository accountRepository;


    @Autowired
    public CourseController(CourseService courseService,
                            CourseRepository courseRepository, LecturerRepository lecturerRepository, AccountRepository accountRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.lecturerRepository = lecturerRepository;
        this.accountRepository = accountRepository;
    }


    private Lecturer getLecturer(Principal principal) {
        //get username of logged in account
        String username = principal.getName();

        //look for the account
        Optional<Account> optionalEntity = accountRepository.findByUsername(username);

        // return the logged in account Entity
        Account account = optionalEntity.orElseThrow();

        // look for the lecturer associated with the account
        Optional<Lecturer> optionalLecturer = lecturerRepository.findByAccount(account);

        // return the associated lecturer entity
        Lecturer lecturer = optionalLecturer.orElseThrow();

        return lecturer;
    }

    // retrieve all the courses of the currently logged in Lecturer
    @GetMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    public ResponseEntity<List<CourseDto>> getAllCourses(Principal principal) {

        Lecturer lecturer = getLecturer(principal);

        List<CourseDto> coursesDto = courseService.getAllCourses(lecturer);

        if (coursesDto != null)
            return new ResponseEntity<>(coursesDto, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Create a new Course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto, Principal principal) {

        Lecturer lecturer = getLecturer(principal);
        courseDto.setLecturer(lecturer);
        CourseDto savedCourse = courseService.createCourse(courseDto);

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

        Lecturer lecturer = getLecturer(principal);

        CourseDto optionalCourse = courseService.getCourseById(id);

        if (null == optionalCourse)
            return new ResponseEntity<>(new MessageResponse("Course was not Found !"), HttpStatus.NOT_FOUND);

        if (lecturer != optionalCourse.getLecturer())
            return new ResponseEntity<>(new MessageResponse("You are not authorized to delete this course, please contact an administrator or check your permissions"), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(new MessageResponse("Course deleted successfully !"), HttpStatus.OK);
    }

    // Update a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable("id") Long id, @RequestBody CourseDto courseDto, Principal principal) {

        // check if the given course exists
        if (null == courseService.getCourseById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // check if Lecturer is permitted to update
        if (courseService.getCourseById(id).getLecturer() != getLecturer(principal))
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        // update Course
        CourseDto updatedCourse = courseService.updateCourse(id, Course.toEntity(courseDto));

        if (updatedCourse == null)
            return new ResponseEntity<>(updatedCourse, HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);

        }

}


