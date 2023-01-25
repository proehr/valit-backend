package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.dto.CourseDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.mapping.CourseDtoMapper;
import com.edu.m7.feedback.model.repository.CourseRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final CourseDtoMapper mapper = Mappers.getMapper(CourseDtoMapper.class);
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDto> getAllCourses(Lecturer lecturer) {
        return courseRepository
                .findByLecturer(lecturer)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    public CourseDto createCourse(CourseDto courseDto, Lecturer lecturer) {
        Course course = mapper.dtoToEntity(courseDto);
        course.setLecturer(lecturer);
        return mapper.entityToDto(courseRepository.save(course));
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto) {

        Course course = courseRepository.findById(id).orElseThrow();
        course = mapper.updateEntityFromDto(courseDto, course);

        return mapper.entityToDto(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public CourseDto getCourseById(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(mapper::entityToDto).orElse(null);
    }

    public Long getLecturerByCourseId(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(course -> course.getLecturer().getLecturerId()).orElseThrow();
    }
}
