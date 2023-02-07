package com.edu.m7.feedback.model.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonRootName("stringAnswerDto")
public class StringAnswerDto extends AnswerDto{

    private String value;
}
