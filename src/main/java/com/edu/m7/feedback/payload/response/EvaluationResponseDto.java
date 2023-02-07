package com.edu.m7.feedback.payload.response;

import com.edu.m7.feedback.model.EvaluationType;
import com.edu.m7.feedback.model.dto.QuestionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EvaluationResponseDto {

    private Long id;
    private String shortcode;
    private Long course;
    private EvaluationType type;
    private String title;
    private LocalDate date;
    private Set<QuestionDto> questions;
    private int participants;

}
