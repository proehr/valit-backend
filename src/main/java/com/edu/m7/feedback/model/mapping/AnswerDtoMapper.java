package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.dto.AnswerDto;
import com.edu.m7.feedback.model.dto.IntAnswerDto;
import com.edu.m7.feedback.model.dto.StringAnswerDto;
import com.edu.m7.feedback.model.entity.Answer;
import com.edu.m7.feedback.model.entity.IntAnswer;
import com.edu.m7.feedback.model.entity.StringAnswer;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AnswerDtoMapper {
    @Mapping(target = "account", ignore = true)
    IntAnswer toIntAnswerEntity(IntAnswerDto answerDto);

    @Mapping(target = "account", ignore = true)
    StringAnswer toStringAnswerEntity(StringAnswerDto answerDto);

    @Mapping(target = "account", ignore = true)
    StringAnswerDto stringAnswerEntityToDto(StringAnswer stringAnswer);

    @Mapping(target = "account", ignore = true)
    IntAnswerDto intAnswerEntityToDto(IntAnswer intAnswer);

    @Mapping(target = "account", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Answer partialUpdate(AnswerDto answerDto, @MappingTarget Answer answer);
}