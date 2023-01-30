package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.QuestionType;
import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.model.dto.ChoiceQuestionDto;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import com.edu.m7.feedback.model.dto.IntAnswerDto;
import com.edu.m7.feedback.model.dto.QuestionChoiceDto;
import com.edu.m7.feedback.model.dto.QuestionDto;
import com.edu.m7.feedback.model.dto.StringAnswerDto;
import com.edu.m7.feedback.model.dto.TextQuestionDto;
import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.model.entity.Answer;
import com.edu.m7.feedback.model.entity.ChoiceQuestion;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.model.entity.IntAnswer;
import com.edu.m7.feedback.model.entity.Question;
import com.edu.m7.feedback.model.entity.QuestionChoice;
import com.edu.m7.feedback.model.entity.StringAnswer;
import com.edu.m7.feedback.model.entity.TextQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface EvaluationDtoMapper {

    EvaluationResponseDto entityToDto(Evaluation entity);

    default Long courseToId(Course course) {
        return course.getId();
    }

    @SubclassMapping(source = StringAnswer.class, target = StringAnswerDto.class)
    @SubclassMapping(source = IntAnswer.class, target = IntAnswerDto.class)
    AnswerDto answerEntityToDto(Answer answer);

    StringAnswerDto stringAnswerEntityToDto(StringAnswer stringAnswer);

    IntAnswerDto intAnswerEntityToDto(IntAnswer intAnswer);

    @SubclassMapping(source = ChoiceQuestion.class, target = ChoiceQuestionDto.class)
    @SubclassMapping(source = TextQuestion.class, target = TextQuestionDto.class)
    @Mapping(source = "question", target = "questionType")
    QuestionDto questionEntityToDto(Question question);

    default QuestionType questionTypeFromEntity(Question question) {
        if (question instanceof ChoiceQuestion) {
            return QuestionType.SCALE;
        }
        return QuestionType.TEXT;
    }

    @Mapping(source = "choiceQuestion", target = "questionType")
    ChoiceQuestionDto choiceQuestionEntityToDto(ChoiceQuestion choiceQuestion);

    QuestionChoiceDto map(QuestionChoice value);
    default Long accountToId(Account account) {
        return account.getAccountId();
    }
}
