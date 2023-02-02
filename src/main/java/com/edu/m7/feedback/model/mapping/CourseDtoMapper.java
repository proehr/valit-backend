package com.edu.m7.feedback.model.mapping;


import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;

@Mapper
public interface CourseDtoMapper {

    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "evaluations", ignore = true)
    Course dtoToEntity(CourseRequestDto dto);

    CourseResponseDto entityToDto(Course entity);

    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "evaluations", ignore = true)
    Course updateEntityFromDto(CourseRequestDto dto, @MappingTarget Course course);

    default LocalDate dateEntityToLocalDate(Date date){
        return date.getLocalDate();
    }
}
