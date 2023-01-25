package com.edu.m7.feedback.model.dto;

import com.edu.m7.feedback.model.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDto {

    private Long id;
    private String questionKey;
    private Integer questionValue;
    private QuestionType type;
    private Set<AnswerDto> answers;
}