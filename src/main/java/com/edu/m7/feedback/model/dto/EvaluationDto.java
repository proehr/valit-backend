package com.edu.m7.feedback.model.dto;

import com.edu.m7.feedback.model.EvaluationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EvaluationDto {

    private Long id;
    private Long course;
    private EvaluationType type;
    private String title;
    private LocalDate date;
    private Set<QuestionDto> questions;

}
