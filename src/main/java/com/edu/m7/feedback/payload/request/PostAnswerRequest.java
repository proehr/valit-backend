package com.edu.m7.feedback.payload.request;


import com.edu.m7.feedback.model.QuestionType;
import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.model.dto.IntAnswerDto;
import com.edu.m7.feedback.model.dto.StringAnswerDto;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PostAnswerRequest {
    private Long id; //questionId
    private QuestionType questionType;
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "questionType")
    @JsonSubTypes({
            @JsonSubTypes.Type(name = "TEXT", value = StringAnswerDto.class),
            @JsonSubTypes.Type(name = "CHOICE", value = IntAnswerDto.class)
    })
    private AnswerDto answer;
}
