package com.edu.m7.feedback.service;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // will execute a SELECT statement, retrieving all the courses associated with a specific foreign key( lecturer id)
    public List<Course> getAllCourses(Lecturer lecturer){
        return  courseRepository.findByLecturer(lecturer);
    }

    //will execute INSERT statement, because course object id is 'null'
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    //will execute an UPDATE statement, because course object is not 'null'
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    // will execute a DELETE statement, targeting the specified id
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Course getCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.orElse(null);
    }

}
