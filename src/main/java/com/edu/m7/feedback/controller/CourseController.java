package com.edu.m7.feedback.controller;

import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import com.edu.m7.feedback.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // return all the courses
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
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course savedCourse = courseService.createCourse(course);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/courses/" + savedCourse.getId());
        return new ResponseEntity<>(savedCourse, headers, HttpStatus.CREATED);
    }

    //API for deleting a course
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    //API for updating a specific course
    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable("id") Long id, @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

}


