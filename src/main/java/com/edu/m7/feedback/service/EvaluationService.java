package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.dto.CourseDto;
import com.edu.m7.feedback.model.dto.EvaluationDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.mapping.CourseDtoMapper;
import com.edu.m7.feedback.model.mapping.EvaluationDtoMapper;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

    private static final EvaluationDtoMapper mapper = Mappers.getMapper(EvaluationDtoMapper.class);
    private static final CourseDtoMapper courseMapper = Mappers.getMapper(CourseDtoMapper.class);

    private final EvaluationRepository repository;

    public EvaluationService(EvaluationRepository repository) {
        this.repository = repository;
    }

    public EvaluationDto loadEvaluationById(Long id) throws UsernameNotFoundException {
        return mapper.entityToDto(repository.findById(id).orElseThrow());
    }

    public Course getCourse(Long id) {
            return repository.findById(id).get().getCourse();
    }

}
