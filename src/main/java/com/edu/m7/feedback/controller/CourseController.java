package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseService courseService,
                            CourseRepository courseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    // return all the courses
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {

        List<Course> courses = courseService.getAllCourses();
        if (courses != null && !courses.isEmpty()) {
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // return all information about a course using an id
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            return new ResponseEntity<>(course, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //API for creating a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course savedCourse = courseService.createCourse(course);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/courses/" + savedCourse.getId());
        return new ResponseEntity<>(savedCourse, headers, HttpStatus.CREATED);
    }

    //API for deleting a course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            courseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //API for updating a specific course
    @RolesAllowed({"ROLE_LECTURER", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable("id") Long id, @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }


}


