package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.EvaluationType;
import com.edu.m7.feedback.model.dto.QuestionChoiceDto;
import com.edu.m7.feedback.model.entity.ChoiceQuestion;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.model.entity.QuestionChoice;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import com.edu.m7.feedback.model.repository.QuestionChoiceRepository;
import com.edu.m7.feedback.model.repository.QuestionRepository;
import com.edu.m7.feedback.payload.QuestionPayloadDtoMapper;
import com.edu.m7.feedback.payload.request.QuestionRequestDto;
import com.edu.m7.feedback.payload.response.QuestionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {
    private static final QuestionPayloadDtoMapper questionMapper = Mappers.getMapper(QuestionPayloadDtoMapper.class);

    private final QuestionRepository questionRepository;
    private final QuestionChoiceRepository questionChoiceRepository;
    private final EvaluationRepository evaluationRepository;

    private final ObjectMapper objectMapper;

    public QuestionService(
            QuestionRepository questionRepository,
            QuestionChoiceRepository questionChoiceRepository,
            EvaluationRepository evaluationRepository, ObjectMapper objectMapper
    ) {
        this.questionRepository = questionRepository;
        this.questionChoiceRepository = questionChoiceRepository;
        this.evaluationRepository = evaluationRepository;
        this.objectMapper = objectMapper;
    }

    public List<QuestionResponseDto> getQuestionsByEvaluationShortCode(String shortCode) {
        Evaluation evaluation = evaluationRepository.findEvaluationByShortcode(shortCode);
        if (evaluation == null) {
            throw new NoSuchElementException("Evaluation not found by shortcode");
        }
        return evaluation.getQuestions()
                .stream()
                .map(questionMapper::map)
                .collect(Collectors.toList());
    }

    public void generateQuestions(EvaluationType evaluationType, Evaluation evaluation) {
        InputStream questionsJson;
        switch (evaluationType) {
            case REGULAR:
                questionsJson = EvaluationService.class
                        .getResourceAsStream("/template/regular-evaluation-questions.json");
                break;
            case FINAL:
                questionsJson = EvaluationService.class
                        .getResourceAsStream("/template/semester-evaluation-questions.json");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + evaluationType);
        }

        QuestionRequestDto[] questionDtos;
        try {
            questionDtos = objectMapper.readValue(questionsJson, QuestionRequestDto[].class);
        } catch (IOException e) {
            log.error("Could not read template questions from file", e);
            throw new IllegalStateException("JSON template for questions could not be parsed");
        }
        for (QuestionRequestDto questionRequestDto : questionDtos) {
            Question question;
            switch (questionRequestDto.getQuestionType()) {
                case TEXT:
                    question = questionMapper.mapToTextQuestion(questionRequestDto);
                    break;
                case CHOICE:
                    question = questionMapper.mapToChoiceQuestion(questionRequestDto);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + questionRequestDto.getQuestionType());
            }
            question.setEvaluation(evaluation);
            questionRepository.save(question);
            if (question instanceof ChoiceQuestion) {
                for (QuestionChoiceDto choiceDto : questionRequestDto.getQuestionChoices()) {
                    QuestionChoice questionChoice = questionMapper.mapToQuestionChoice(choiceDto);
                    questionChoice.setQuestion((ChoiceQuestion) question);
                    questionChoiceRepository.save(questionChoice);
                }
            }
        }
    }

    Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow();
    }
}
