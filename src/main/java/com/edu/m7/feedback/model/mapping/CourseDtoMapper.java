package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.dto.CourseDto;
import com.edu.m7.feedback.model.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface CourseDtoMapper {

    Course dtoToEntity(CourseDto dto);
    CourseDto entityToDto(Course entity);

    Course updateEntityFromDto(CourseDto dto, @MappingTarget Course course);

}
