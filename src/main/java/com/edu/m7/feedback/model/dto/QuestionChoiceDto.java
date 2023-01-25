package com.edu.m7.feedback.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class QuestionChoiceDto {

    private String choiceKey;
    private Integer choiceValue;
}
