package com.edu.m7.feedback.model.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonRootName("intAnswerDto")
public class IntAnswerDto extends AnswerDto {

    private Integer value;
}
