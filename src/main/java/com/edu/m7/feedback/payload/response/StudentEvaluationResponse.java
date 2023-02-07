package com.edu.m7.feedback.payload.response;

import com.edu.m7.feedback.model.EvaluationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StudentEvaluationResponse {

    private List<QuestionResponseDto> questions;
    private EvaluationType evaluationType;
    private LocalTime endTimeOfLiveFeature;

}
