package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.mapping.EvaluationHeaderResponseMapper;
import com.edu.m7.feedback.payload.QuestionResponseDtoMapper;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.payload.response.EvaluationHeaderResponse;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.model.dto.QuestionDto;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.model.mapping.EvaluationDtoMapper;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import com.edu.m7.feedback.payload.response.QuestionResponseDto;
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

    private static final EvaluationDtoMapper evaluationMapper = Mappers.getMapper(EvaluationDtoMapper.class);
    private static final EvaluationHeaderResponseMapper EvaluationHeaderMapper = Mappers.getMapper(EvaluationHeaderResponseMapper.class);
    private static final QuestionResponseDtoMapper questionMapper = Mappers.getMapper(QuestionResponseDtoMapper.class);
    private final EvaluationRepository evaluationRepository;
    private final CourseService courseService;

    public EvaluationService(EvaluationRepository evaluationRepository, CourseService courseService) {
        this.evaluationRepository = evaluationRepository;
        this.courseService = courseService;
    }

    public EvaluationResponseDto loadEvaluationById(Long id) throws UsernameNotFoundException {
        return evaluationMapper.map(evaluationRepository.findById(id).orElseThrow());
    }

    public EvaluationHeaderResponse loadEvaluationHeaderById(Long id, Long courseId) throws UsernameNotFoundException {
        Evaluation evaluation = evaluationRepository.findById(id).orElseThrow();
        CourseResponseDto course = courseService.getCourseById(courseId);
        EvaluationHeaderResponse evaluationHeaderResponse = EvaluationHeaderMapper.map(evaluation);
        evaluationHeaderResponse.setCourseName(course.getName());
        String participants = String.valueOf(evaluation.getQuestions().iterator().next().getAnswers().size());
        evaluationHeaderResponse.setParticipants(participants + "/" + course.getStudentCount());
        return evaluationHeaderResponse;
    }

    public Long getLecturerIdByEvaluationId(Long id) {
        Optional<Evaluation> optionalEvaluation = evaluationRepository.findById(id);
        return optionalEvaluation.map(evaluation -> evaluation.getCourse().getLecturer().getLecturerId()).orElseThrow();
    }

    public EvaluationResponseDto loadEvaluationResultByParticipant(Long id, Long participantId) {
        EvaluationResponseDto evaluation = evaluationMapper.entityToDto(evaluationRepository.findById(id).orElseThrow());
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
        Evaluation evaluation = evaluationRepository.findById(id).orElseThrow();
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
       Evaluation evaluation = evaluationRepository.findEvaluationByShortcode(shortcode);
       return evaluationMapper.map(evaluation);
    }

    public List<QuestionResponseDto> getQuestions(Integer shortCode){
        Evaluation evaluation = evaluationRepository.findEvaluationByShortcode(shortCode);
        if(evaluation == null){
            throw new NoSuchElementException("Evaluation not found by shortcode");
        }
        return evaluation.getQuestions()
                .stream()
                .map(questionMapper::map)
                .collect(Collectors.toList());
    }
}
