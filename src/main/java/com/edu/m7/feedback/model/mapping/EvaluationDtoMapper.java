package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.model.dto.EvaluationDto;
import com.edu.m7.feedback.model.dto.IntAnswerDto;
import com.edu.m7.feedback.model.dto.QuestionDto;
import com.edu.m7.feedback.model.dto.StringAnswerDto;
import com.edu.m7.feedback.model.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface EvaluationDtoMapper {

    EvaluationDto entityToDto(Evaluation entity);

    default Long courseToId(Course course) {
        return course.getId();
    }

    QuestionDto questionEntityToDto(Question question);

    @SubclassMapping(source = StringAnswer.class, target = StringAnswerDto.class)
    @SubclassMapping(source = IntAnswer.class, target = IntAnswerDto.class)
    AnswerDto answerEntityToDto(Answer answer);
    StringAnswerDto stringAnswerEntityToDto(StringAnswer stringAnswer);
    IntAnswerDto intAnswerEntityToDto(IntAnswer intAnswer);
    default Long accountToId(Account account) {
        return account.getAccountId();
    }
}
