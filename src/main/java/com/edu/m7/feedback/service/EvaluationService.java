package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.model.dto.QuestionDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.model.mapping.EvaluationDtoMapper;
import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    private static final EvaluationDtoMapper mapper = Mappers.getMapper(EvaluationDtoMapper.class);

    private final CourseRepository courseRepository;
    private final EvaluationRepository repository;

    public EvaluationService(CourseRepository courseRepository, EvaluationRepository repository) {
        this.courseRepository = courseRepository;
        this.repository = repository;
    }

    public EvaluationResponseDto loadEvaluationById(Long id) throws UsernameNotFoundException {
        return mapper.entityToDto(repository.findById(id).orElseThrow());
    }

    public Long getLecturerIdByEvaluationId(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(course -> course.getLecturer().getLecturerId()).orElseThrow();
    }

    public EvaluationResponseDto loadEvaluationResultByParticipant(Long id, Long participantId) {
        EvaluationResponseDto evaluation = mapper.entityToDto(repository.findById(id).orElseThrow());
        for (QuestionDto question : evaluation.getQuestions()) {
            Set<AnswerDto> participantAnswer = question
                    .getAnswers()
                    .stream()
                    .filter(answer -> answer.getAccount().equals(participantId)).collect(Collectors.toSet());
            if(participantAnswer.size() > 1){
                throw new IllegalStateException("Participant has handed in multiple answers for a question.");
            }
            if (participantAnswer.isEmpty()){
                throw new NoSuchElementException("Participant has not handed in an answer for this evaluation");
            }
            question.setAnswers(participantAnswer);
        }
        return evaluation;
    }

    public List<Long> getParticipants(Long id){
        Evaluation evaluation = repository.findById(id).orElseThrow();
        if(!evaluation.getQuestions().isEmpty()){
            Question question = evaluation.getQuestions().iterator().next();
             return question
                     .getAnswers()
                     .stream()
                     .map((answer -> answer.getAccount().getAccountId())).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
    public EvaluationResponseDto getEvaluationByShortcode(Integer shortcode) {
       Evaluation evaluation = repository.findEvaluationByShortcode(shortcode);
       return mapper.entityToDto(evaluation);
    }
}
