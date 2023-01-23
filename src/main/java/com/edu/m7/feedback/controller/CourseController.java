package com.edu.m7.feedback.controller;

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
        Account account  = optionalEntity.orElseThrow();

        // look for the lecturer associated with the account
        Optional<Lecturer> optionalLecturer = lecturerRepository.findByAccount(account);

        // return the associated lecturer entity
        Lecturer lecturer = optionalLecturer.orElseThrow();

        return lecturer;
    }

    // retrieve all the courses of the currently logged in Lecturer
    @GetMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_LECTURER"})
    public ResponseEntity<List<Course>> getAllCourses(Principal principal) {

        Lecturer lecturer = getLecturer(principal);

        List<Course> courses = courseService.getAllCourses(lecturer);

        if (courses != null && !courses.isEmpty()) {
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // Create a new Course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course, Principal principal) {

        Lecturer lecturer = getLecturer(principal);

        course.setLecturer(lecturer);

        Course savedCourse = courseService.createCourse(course);

        if (savedCourse != null){
            return new ResponseEntity<>(savedCourse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // Delete a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id, Principal principal) {

        Lecturer lecturer = getLecturer(principal);

        Optional<Course> course = courseRepository.findById(id);

        if (course.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Course not Found !"), HttpStatus.NOT_FOUND);

        if (course.isPresent() && lecturer != course.get().getLecturer())
            return new ResponseEntity<>(new MessageResponse("Cannot delete Course: Not authorized "),HttpStatus.UNAUTHORIZED);

        courseRepository.deleteById(id);

        return new ResponseEntity<>(new MessageResponse("Course successfully deleted !"), HttpStatus.OK);
    }

    // Update a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") Long id, @RequestBody Course course, Principal principal) {

        Lecturer lecturer = getLecturer(principal);

        Course existingCourse = courseService.getCourseById(id);

        if(existingCourse.getLecturer() != lecturer)
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        if (existingCourse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingCourse.setName(course.getName());
        existingCourse.setDegree(course.getDegree());
        existingCourse.setStudentCount(course.getStudentCount());
        existingCourse.setTimeStart(course.getTimeStart());
        existingCourse.setTimeEnd(course.getTimeEnd());
        existingCourse.setDates(course.getDates());
        existingCourse.setStudentCount(course.getStudentCount());
        existingCourse.setLecturer(lecturer);
        existingCourse.setEvaluations(course.getEvaluations());

        Course updatedCourse = courseService.updateCourse(existingCourse);

    if(updatedCourse == null)
        return new ResponseEntity<>(updatedCourse, HttpStatus.INTERNAL_SERVER_ERROR);

    return new ResponseEntity<>(updatedCourse, HttpStatus.OK);

    }


}


