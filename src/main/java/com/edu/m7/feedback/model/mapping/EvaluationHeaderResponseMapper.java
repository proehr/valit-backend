package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.entity.Evaluation;
import com.edu.m7.feedback.payload.response.EvaluationHeaderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EvaluationHeaderResponseMapper {

    @Mapping(target = "courseName", ignore = true)
    @Mapping(target = "participants", ignore = true)
    EvaluationHeaderResponse map(Evaluation evaluation);
}
