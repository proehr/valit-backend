package com.edu.m7.feedback.model.dto;

import com.edu.m7.feedback.model.VisualizationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChoiceQuestionDto extends QuestionDto {

    private VisualizationType visualizationType;
    private Set<QuestionChoiceDto> choices;
}
