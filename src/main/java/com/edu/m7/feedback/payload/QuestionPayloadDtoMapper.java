package com.edu.m7.feedback.payload;

import com.edu.m7.feedback.model.QuestionType;
import com.edu.m7.feedback.model.dto.QuestionChoiceDto;
import com.edu.m7.feedback.model.entity.ChoiceQuestion;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.model.entity.QuestionChoice;
import com.edu.m7.feedback.model.entity.TextQuestion;
import com.edu.m7.feedback.payload.request.QuestionRequestDto;
import com.edu.m7.feedback.payload.response.ChoiceQuestionResponseDto;
import com.edu.m7.feedback.payload.response.QuestionResponseDto;
import com.edu.m7.feedback.payload.response.TextQuestionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper
public interface QuestionPayloadDtoMapper {

    @SubclassMapping(source = ChoiceQuestion.class, target = ChoiceQuestionResponseDto.class)
    @SubclassMapping(source = TextQuestion.class, target = TextQuestionResponseDto.class)
    @Mapping(source = "question", target = "questionType")
    QuestionResponseDto map(Question question);

    default QuestionType questionTypeFromEntity(Question question) {
        if (question instanceof ChoiceQuestion) {
            return QuestionType.CHOICE;
        }
        return QuestionType.TEXT;
    }

    ChoiceQuestion mapToChoiceQuestion(QuestionRequestDto dto);

    TextQuestion mapToTextQuestion(QuestionRequestDto dto);

    QuestionChoice mapToQuestionChoice(QuestionChoiceDto dto);
}
