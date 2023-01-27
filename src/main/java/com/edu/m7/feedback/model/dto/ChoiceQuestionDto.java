package com.edu.m7.feedback.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChoiceQuestionDto extends QuestionDto {

    private Set<QuestionChoiceDto> choices;
}
