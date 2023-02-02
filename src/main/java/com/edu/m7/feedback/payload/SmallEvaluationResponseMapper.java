package com.edu.m7.feedback.payload;

import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.payload.response.SmallEvaluationResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface SmallEvaluationResponseMapper {

    @Mapping(target = "participants", source = "questions")
    SmallEvaluationResponseDto map(Evaluation evaluation);

    default int questionsToParticipantCount(Iterable<Question> questions){
        return questions.iterator().next().getAnswers().size();
    }
}
