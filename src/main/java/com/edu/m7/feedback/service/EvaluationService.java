package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.EvaluationType;
import com.edu.m7.feedback.model.QuestionType;
import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.model.dto.IntAnswerDto;
import com.edu.m7.feedback.model.dto.StringAnswerDto;
import com.edu.m7.feedback.model.entity.*;
import com.edu.m7.feedback.model.mapping.AnswerDtoMapper;
import com.edu.m7.feedback.model.mapping.EvaluationHeaderResponseMapper;
import com.edu.m7.feedback.model.repository.AnswerRepository;
import com.edu.m7.feedback.payload.request.PostAnswerRequest;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.payload.response.EvaluationHeaderResponse;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.model.dto.QuestionDto;
import com.edu.m7.feedback.model.mapping.EvaluationDtoMapper;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import com.edu.m7.feedback.util.ShortCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EvaluationService {

    private static final EvaluationDtoMapper evaluationMapper = Mappers.getMapper(EvaluationDtoMapper.class);
    private static final AnswerDtoMapper answerMapper = Mappers.getMapper(AnswerDtoMapper.class);
    private static final EvaluationHeaderResponseMapper EvaluationHeaderMapper =
            Mappers.getMapper(EvaluationHeaderResponseMapper.class);
    private final EvaluationRepository evaluationRepository;
    private final AnswerRepository answerRepository;
    private final CourseService courseService;
    private final QuestionService questionService;

    @Value("${valit.app.shortCodeLength}")
    private int shortCodeLength;

    public EvaluationService(
            EvaluationRepository evaluationRepository,
            AnswerRepository answerRepository, CourseService courseService,
            QuestionService questionService) {
        this.evaluationRepository = evaluationRepository;
        this.answerRepository = answerRepository;
        this.courseService = courseService;
        this.questionService = questionService;
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
        evaluationHeaderResponse.setParticipants(participants + " / " + course.getStudentCount());
        return evaluationHeaderResponse;
    }

    public Long getLecturerIdByEvaluationId(Long id) {
        Optional<Evaluation> optionalEvaluation = evaluationRepository.findById(id);
        return optionalEvaluation.map(evaluation -> evaluation.getCourse().getLecturer().getLecturerId()).orElseThrow();
    }

    public EvaluationResponseDto loadEvaluationResultByParticipant(Long id, Long participantId) {
        EvaluationResponseDto evaluation = evaluationMapper.entityToDto(
                evaluationRepository.findById(id).orElseThrow()
        );
        for (QuestionDto question : evaluation.getQuestions()) {
            Set<AnswerDto> participantAnswer = question
                    .getAnswers()
                    .stream()
                    .filter(answer -> answer.getAccount().equals(participantId)).collect(Collectors.toSet());
            if (participantAnswer.size() > 1) {
                throw new IllegalStateException("Participant has handed in multiple answers for a question.");
            }
            if (participantAnswer.isEmpty()) {
                throw new NoSuchElementException("Participant has not handed in an answer for this evaluation");
            }
            question.setAnswers(participantAnswer);
        }
        return evaluation;
    }

    public List<Long> getParticipants(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id).orElseThrow();
        if (!evaluation.getQuestions().isEmpty()) {
            Question question = evaluation.getQuestions().iterator().next();
            return question
                    .getAnswers()
                    .stream()
                    .map((answer -> answer.getAccount().getAccountId())).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public EvaluationResponseDto getEvaluationByShortcode(String shortcode) {
        Evaluation evaluation = evaluationRepository.findEvaluationByShortcode(shortcode);
        return evaluationMapper.map(evaluation);
    }

    public void createEvaluation(Course course, LocalDate date, EvaluationType evaluationType) {
        Evaluation evaluation = new Evaluation();
        evaluation.setActive(true);
        evaluation.setType(evaluationType);
        evaluation.setDate(date);
        evaluation.setCourse(course);
        evaluation.setTitle("");
        String shortCode;
        do {
            shortCode = ShortCodeUtil.generate(shortCodeLength);
        } while (evaluationRepository.existsByShortcode(shortCode));
        evaluation.setShortcode(shortCode);
        evaluationRepository.save(evaluation);

        questionService.generateQuestions(evaluationType, evaluation);
    }

    public void postAnswers(List<PostAnswerRequest> postAnswerRequests, Account account) {
        for (PostAnswerRequest postAnswerRequest : postAnswerRequests) {
            Question question = questionService.getQuestionById(postAnswerRequest.getId());
            if (postAnswerRequest.getQuestionType().equals(QuestionType.TEXT)) {
                StringAnswerDto stringAnswerDto = (StringAnswerDto) postAnswerRequest.getAnswer();
                StringAnswer stringAnswer = answerMapper.toStringAnswerEntity(stringAnswerDto);
                stringAnswer.setQuestion(question);
                stringAnswer.setAccount(account);
                answerRepository.save(stringAnswer);
            } else {
                IntAnswerDto intAnswerDto = (IntAnswerDto) postAnswerRequest.getAnswer();
                IntAnswer intAnswer = answerMapper.toIntAnswerEntity(intAnswerDto);
                intAnswer.setQuestion(question);
                intAnswer.setAccount(account);
                answerRepository.save(intAnswer);
            }
        }
    }
}
