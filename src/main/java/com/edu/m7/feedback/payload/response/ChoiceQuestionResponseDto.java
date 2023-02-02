package com.edu.m7.feedback.payload.response;

import com.edu.m7.feedback.model.dto.QuestionChoiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChoiceQuestionResponseDto extends QuestionResponseDto {

    private Set<QuestionChoiceDto> choices;
}
