package com.edu.m7.feedback.service;
import com.edu.m7.feedback.model.dto.CourseDto;
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

    public List<CourseDto> getAllCourses(Lecturer lecturer){
        Optional<List<Course>> optionalCourse = this.courseRepository.findByLecturer(lecturer);
        if(optionalCourse.isPresent())
            return CourseDto.toDto(optionalCourse.get());
        else
            return null;
    }

    public CourseDto createCourse(CourseDto course) {
        return CourseDto.toDto(courseRepository.save(Course.toEntity(course)));
    }

    public CourseDto updateCourse(Long id, Course newCourse){

        Course course = courseRepository.findById(id).orElseThrow();

        // We update everything but the id and the lecturer
        course.setName(newCourse.getName());
        course.setDegree(newCourse.getDegree());
        course.setStudentCount(newCourse.getStudentCount());
        course.setTimeStart(newCourse.getTimeStart());
        course.setTimeEnd(newCourse.getTimeEnd());
        course.setDates(newCourse.getDates());
        course.setStudentCount(newCourse.getStudentCount());
        course.setEvaluations(newCourse.getEvaluations());

        return CourseDto.toDto(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public CourseDto getCourseById(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isPresent())
            return CourseDto.toDto(optionalCourse.get());
        else
            return null;
    }

}
