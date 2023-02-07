package com.edu.m7.feedback.payload.request;

import com.edu.m7.feedback.model.QuestionType;
import com.edu.m7.feedback.model.VisualizationType;
import com.edu.m7.feedback.model.dto.QuestionChoiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionRequestDto {

    private String questionKey;
    private Integer questionPosition;
    private Integer sectionNumber;
    private QuestionType questionType;
    private QuestionChoiceDto[] questionChoices;
    private VisualizationType visualizationType;

}
