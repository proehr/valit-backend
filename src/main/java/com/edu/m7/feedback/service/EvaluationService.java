package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.dto.EvaluationDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.mapping.CourseDtoMapper;
import com.edu.m7.feedback.model.mapping.EvaluationDtoMapper;
import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import com.edu.m7.feedback.model.repository.LecturerRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluationService {

    private static final EvaluationDtoMapper mapper = Mappers.getMapper(EvaluationDtoMapper.class);
    private static final CourseDtoMapper courseMapper = Mappers.getMapper(CourseDtoMapper.class);

    private final LecturerRepository lecturerRepository;
    private final CourseRepository courseRepository;

    private final EvaluationRepository repository;

    public EvaluationService(LecturerRepository lecturerRepository, CourseRepository courseRepository, EvaluationRepository repository) {
        this.lecturerRepository = lecturerRepository;
        this.courseRepository = courseRepository;
        this.repository = repository;
    }

    public EvaluationDto loadEvaluationById(Long id) throws UsernameNotFoundException {
        return mapper.entityToDto(repository.findById(id).orElseThrow());
    }

    public Course getCourse(Long id) {
            return repository.findById(id).get().getCourse();
    }
    public Long getLecturerIdByEvaluationId(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(course -> course.getLecturer().getLecturerId()).orElseThrow();
    }

}
