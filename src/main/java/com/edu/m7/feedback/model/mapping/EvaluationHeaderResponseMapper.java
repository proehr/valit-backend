package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.payload.response.EvaluationHeaderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EvaluationHeaderResponseMapper {

    @Mapping(target = "participants", source = "questions")
    EvaluationHeaderResponse map(Evaluation evaluation);

    default int questionsToParticipantCount(Iterable<Question> questions){
        return questions.iterator().next().getAnswers().size();
    }

    //SmallEvaluationResponseDto entityToDto(Evaluation entity);
}
