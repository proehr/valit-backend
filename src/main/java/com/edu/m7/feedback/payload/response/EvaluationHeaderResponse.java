package com.edu.m7.feedback.payload.response;

import com.edu.m7.feedback.model.EvaluationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class EvaluationHeaderResponse {
    private Long id;
    private EvaluationType type;
    private String title;
    private LocalDate date;
    private int participants; //hier als String
}
